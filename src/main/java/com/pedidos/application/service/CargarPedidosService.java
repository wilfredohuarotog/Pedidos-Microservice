package com.pedidos.application.service;

import com.pedidos.application.dto.*;
import com.pedidos.domain.exception.IdempotenciaException;
import com.pedidos.domain.model.*;
import com.pedidos.domain.port.in.CargarPedidosUseCase;
import com.pedidos.domain.port.out.*;
import com.pedidos.domain.service.*;
import com.pedidos.infrastructure.csv.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CargarPedidosService implements CargarPedidosUseCase {

    private final CsvProcessor csvProcessor;
    private final PedidoValidator validator;
    private final IdempotenciaService idempotenciaService;

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ZonaRepository zonaRepository;
    private final IdempotenciaRepository idempotenciaRepository;

    @Value("${batch.size:500}")
    private int batchSize;

    @Override
    @Transactional
    public CargaResultado cargarPedidos(MultipartFile archivo, String idempotencyKey) {
        log.info("Iniciando carga de pedidos. IdempotencyKey: {}", idempotencyKey);

        // Validar idempotencia
        String archivoHash = idempotenciaService.calcularHashArchivo(archivo);
        Optional<CargaIdempotencia> cargaPrevia = idempotenciaRepository
                .buscarPorKeyYHash(idempotencyKey, archivoHash);

        if (cargaPrevia.isPresent()) {
            log.warn("Carga duplicada detectada. Key: {}, Hash: {}", idempotencyKey, archivoHash);
            throw new IdempotenciaException("Carga ya procesada previamente");
        }

        // Procesar CSV
        List<PedidoCsvRow> filas = csvProcessor.procesarArchivo(archivo);
        log.info("Total de filas leídas del CSV: {}", filas.size());

        // Cargar catálogos en batch
        Set<String> clienteIds = filas.stream()
                .map(PedidoCsvRow::getClienteId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> zonaIds = filas.stream()
                .map(PedidoCsvRow::getZonaEntrega)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> numerosPedidos = filas.stream()
                .map(PedidoCsvRow::getNumeroPedido)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<String, Cliente> clientesCache = clienteRepository.obtenerClientesPorIds(clienteIds)
                .stream()
                .collect(Collectors.toMap(Cliente::getId, c -> c));

        Map<String, Zona> zonasCache = zonaRepository.obtenerZonasPorIds(zonaIds)
                .stream()
                .collect(Collectors.toMap(Zona::getId, z -> z));

        Set<String> numerosPedidosExistentes = pedidoRepository
                .obtenerNumerosPedidosExistentes(numerosPedidos);

        // Validar y procesar en lotes
        List<Pedido> pedidosValidos = new ArrayList<>();
        List<ErrorDetalle> errores = new ArrayList<>();
        Map<String, Integer> erroresAgrupados = new HashMap<>();

        for (int i = 0; i < filas.size(); i++) {
            PedidoCsvRow fila = filas.get(i);
            int numeroLinea = i + 2; // +2 por header y porque empieza en 0

            List<String> erroresValidacion = validator.validar(
                    fila.getNumeroPedido(),
                    fila.getClienteId(),
                    fila.getZonaEntrega(),
                    fila.getFechaEntrega(),
                    fila.getEstado(),
                    fila.isRequiereRefrigeracion(),
                    clientesCache,
                    zonasCache,
                    numerosPedidosExistentes
            );

            if (erroresValidacion.isEmpty()) {
                Pedido pedido = Pedido.crear(
                        fila.getNumeroPedido(),
                        fila.getClienteId(),
                        fila.getZonaEntrega(),
                        fila.getFechaEntrega(),
                        fila.getEstado(),
                        fila.isRequiereRefrigeracion()
                );
                pedidosValidos.add(pedido);
                numerosPedidosExistentes.add(fila.getNumeroPedido()); // Evitar duplicados en el mismo archivo
            } else {
                ErrorDetalle error = ErrorDetalle.builder()
                        .linea(numeroLinea)
                        .numeroPedido(fila.getNumeroPedido())
                        .motivos(erroresValidacion)
                        .build();
                errores.add(error);

                // Agrupar errores
                erroresValidacion.forEach(tipo ->
                        erroresAgrupados.merge(tipo, 1, Integer::sum)
                );
            }
        }

        // Guardar en lotes
        log.info("Guardando {} pedidos válidos en lotes de {}", pedidosValidos.size(), batchSize);
        for (int i = 0; i < pedidosValidos.size(); i += batchSize) {
            int end = Math.min(i + batchSize, pedidosValidos.size());
            List<Pedido> lote = pedidosValidos.subList(i, end);
            pedidoRepository.guardarLote(lote);
            log.debug("Guardado lote {}/{}", (i / batchSize) + 1,
                    (pedidosValidos.size() + batchSize - 1) / batchSize);
        }

        // Registrar carga como procesada
        CargaIdempotencia carga = CargaIdempotencia.crear(idempotencyKey, archivoHash);
        idempotenciaRepository.guardar(carga);

        log.info("Carga finalizada. Válidos: {}, Errores: {}", pedidosValidos.size(), errores.size());

        return CargaResultado.builder()
                .totalProcesados(filas.size())
                .guardados(pedidosValidos.size())
                .conError(errores.size())
                .erroresDetalle(errores)
                .erroresAgrupados(erroresAgrupados)
                .build();
    }
}
