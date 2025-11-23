package com.pedidos.domain.service;

import com.pedidos.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PedidoValidator {

    private static final String TIMEZONE = "America/Lima";

    public List<String> validar(String numeroPedido, String clienteId, String zonaId,
                                LocalDate fechaEntrega, EstadoPedido estado,
                                boolean requiereRefrigeracion,
                                Map<String, Cliente> clientesCache,
                                Map<String, Zona> zonasCache,
                                Set<String> numerosPedidosExistentes) {

        List<String> errores = new ArrayList<>();

        // Validar número de pedido
        if (numeroPedido == null || numeroPedido.isBlank()) {
            errores.add("NUMERO_PEDIDO_VACIO");
        } else if (!numeroPedido.matches("^[a-zA-Z0-9]+$")) {
            errores.add("NUMERO_PEDIDO_INVALIDO");
        } else if (numerosPedidosExistentes.contains(numeroPedido)) {
            errores.add("DUPLICADO");
        }

        // Validar cliente
        if (clienteId == null || clienteId.isBlank()) {
            errores.add("CLIENTE_ID_VACIO");
        } else {
            Cliente cliente = clientesCache.get(clienteId);
            if (cliente == null || !cliente.isActivo()) {
                errores.add("CLIENTE_NO_ENCONTRADO");
            }
        }

        // Validar fecha de entrega
        if (fechaEntrega == null) {
            errores.add("FECHA_INVALIDA");
        } else {
            LocalDate hoy = LocalDate.now(ZoneId.of(TIMEZONE));
            if (fechaEntrega.isBefore(hoy)) {
                errores.add("FECHA_INVALIDA");
            }
        }

        // Validar estado
        if (estado == null) {
            errores.add("ESTADO_INVALIDO");
        }

        // Validar zona y refrigeración
        if (zonaId == null || zonaId.isBlank()) {
            errores.add("ZONA_VACIA");
        } else {
            Zona zona = zonasCache.get(zonaId);
            if (zona == null) {
                errores.add("ZONA_INVALIDA");
            } else if (requiereRefrigeracion && !zona.isSoporteRefrigeracion()) {
                errores.add("CADENA_FRIO_NO_SOPORTADA");
            }
        }

        return errores;
    }
}