package com.pedidos.domain.service;

import com.pedidos.domain.model.Cliente;
import com.pedidos.domain.model.EstadoPedido;
import com.pedidos.domain.model.Zona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class PedidoValidatorTest {

    private PedidoValidator validator;
    private Map<String, Cliente> clientesCache;
    private Map<String, Zona> zonasCache;
    private Set<String> numerosPedidosExistentes;

    @BeforeEach
    void setUp() {
        validator = new PedidoValidator();

        clientesCache = new HashMap<>();
        clientesCache.put("CLI-001", Cliente.builder().id("CLI-001").activo(true).build());

        zonasCache = new HashMap<>();
        zonasCache.put("ZONA1", Zona.builder().id("ZONA1").soporteRefrigeracion(true).build());
        zonasCache.put("ZONA2", Zona.builder().id("ZONA2").soporteRefrigeracion(false).build());

        numerosPedidosExistentes = new HashSet<>();
    }

    @Test
    @DisplayName("Debe validar correctamente un pedido válido")
    void debeValidarPedidoValido() {
        // Given
        String numeroPedido = "P001";
        String clienteId = "CLI-001";
        String zonaId = "ZONA1";
        LocalDate fechaEntrega = LocalDate.now(ZoneId.of("America/Lima")).plusDays(5);
        EstadoPedido estado = EstadoPedido.PENDIENTE;
        boolean requiereRefrigeracion = true;

        // When
        List<String> errores = validator.validar(
                numeroPedido, clienteId, zonaId, fechaEntrega, estado,
                requiereRefrigeracion, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).isEmpty();
    }

    @Test
    @DisplayName("Debe rechazar número de pedido duplicado")
    void debeRechazarNumeroPedidoDuplicado() {
        // Given
        numerosPedidosExistentes.add("P001");
        LocalDate fechaEntrega = LocalDate.now(ZoneId.of("America/Lima")).plusDays(1);

        // When
        List<String> errores = validator.validar(
                "P001", "CLI-001", "ZONA1", fechaEntrega, EstadoPedido.PENDIENTE,
                false, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).contains("DUPLICADO");
    }

    @Test
    @DisplayName("Debe rechazar número de pedido inválido")
    void debeRechazarNumeroPedidoInvalido() {
        // Given
        String numeroPedidoInvalido = "P-001!@";
        LocalDate fechaEntrega = LocalDate.now(ZoneId.of("America/Lima")).plusDays(1);

        // When
        List<String> errores = validator.validar(
                numeroPedidoInvalido, "CLI-001", "ZONA1", fechaEntrega, EstadoPedido.PENDIENTE,
                false, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).contains("NUMERO_PEDIDO_INVALIDO");
    }

    @Test
    @DisplayName("Debe rechazar cliente no encontrado")
    void debeRechazarClienteNoEncontrado() {
        // Given
        LocalDate fechaEntrega = LocalDate.now(ZoneId.of("America/Lima")).plusDays(1);

        // When
        List<String> errores = validator.validar(
                "P001", "CLI-999", "ZONA1", fechaEntrega, EstadoPedido.PENDIENTE,
                false, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).contains("CLIENTE_NO_ENCONTRADO");
    }

    @Test
    @DisplayName("Debe rechazar fecha de entrega pasada")
    void debeRechazarFechaPasada() {
        // Given
        LocalDate fechaPasada = LocalDate.now(ZoneId.of("America/Lima")).minusDays(1);

        // When
        List<String> errores = validator.validar(
                "P001", "CLI-001", "ZONA1", fechaPasada, EstadoPedido.PENDIENTE,
                false, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).contains("FECHA_INVALIDA");
    }

    @Test
    @DisplayName("Debe rechazar zona sin soporte de refrigeración cuando se requiere")
    void debeRechazarZonaSinRefrigeracion() {
        // Given
        LocalDate fechaEntrega = LocalDate.now(ZoneId.of("America/Lima")).plusDays(1);

        // When
        List<String> errores = validator.validar(
                "P001", "CLI-001", "ZONA2", fechaEntrega, EstadoPedido.PENDIENTE,
                true, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).contains("CADENA_FRIO_NO_SOPORTADA");
    }

    @Test
    @DisplayName("Debe permitir zona sin refrigeración cuando no se requiere")
    void debePermitirZonaSinRefrigeracionCuandoNoSeRequiere() {
        // Given
        LocalDate fechaEntrega = LocalDate.now(ZoneId.of("America/Lima")).plusDays(1);

        // When
        List<String> errores = validator.validar(
                "P001", "CLI-001", "ZONA2", fechaEntrega, EstadoPedido.PENDIENTE,
                false, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).isEmpty();
    }

    @Test
    @DisplayName("Debe rechazar zona inválida")
    void debeRechazarZonaInvalida() {
        // Given
        LocalDate fechaEntrega = LocalDate.now(ZoneId.of("America/Lima")).plusDays(1);

        // When
        List<String> errores = validator.validar(
                "P001", "CLI-001", "ZONA999", fechaEntrega, EstadoPedido.PENDIENTE,
                false, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).contains("ZONA_INVALIDA");
    }

    @Test
    @DisplayName("Debe rechazar estado nulo")
    void debeRechazarEstadoNulo() {
        // Given
        LocalDate fechaEntrega = LocalDate.now(ZoneId.of("America/Lima")).plusDays(1);

        // When
        List<String> errores = validator.validar(
                "P001", "CLI-001", "ZONA1", fechaEntrega, null,
                false, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).contains("ESTADO_INVALIDO");
    }

    @Test
    @DisplayName("Debe acumular múltiples errores")
    void debeAcumularMultiplesErrores() {
        // Given
        LocalDate fechaPasada = LocalDate.now(ZoneId.of("America/Lima")).minusDays(1);

        // When
        List<String> errores = validator.validar(
                "P-001!", "CLI-999", "ZONA999", fechaPasada, null,
                true, clientesCache, zonasCache, numerosPedidosExistentes
        );

        // Then
        assertThat(errores).hasSizeGreaterThanOrEqualTo(4);
        assertThat(errores).contains("NUMERO_PEDIDO_INVALIDO");
        assertThat(errores).contains("CLIENTE_NO_ENCONTRADO");
        assertThat(errores).contains("FECHA_INVALIDA");
        assertThat(errores).contains("ESTADO_INVALIDO");
    }
}