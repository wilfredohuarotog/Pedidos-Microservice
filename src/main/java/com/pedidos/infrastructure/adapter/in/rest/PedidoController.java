package com.pedidos.infrastructure.adapter.in.rest;

import com.pedidos.application.dto.CargaResultado;
import com.pedidos.domain.port.in.CargarPedidosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API para carga de pedidos desde CSV")
@SecurityRequirement(name = "bearer-jwt")
public class PedidoController {

    private final CargarPedidosUseCase cargarPedidosUseCase;

    @PostMapping(value = "/cargar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Cargar pedidos desde archivo CSV",
            description = "Procesa un archivo CSV con pedidos, valida cada registro y persiste los válidos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Carga procesada exitosamente",
                            content = @Content(schema = @Schema(implementation = CargaResultado.class))),
                    @ApiResponse(responseCode = "400", description = "Archivo inválido o error de validación"),
                    @ApiResponse(responseCode = "409", description = "Carga duplicada (idempotencia)"),
                    @ApiResponse(responseCode = "401", description = "No autorizado")
            }
    )
    public ResponseEntity<CargaResultado> cargarPedidos(
            @Parameter(description = "Archivo CSV con los pedidos", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Clave de idempotencia para evitar duplicados", required = true)
            @RequestHeader("Idempotency-Key") String idempotencyKey
    ) {
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        try {
            log.info("Recibida solicitud de carga. File: {}, Size: {} bytes, IdempotencyKey: {}",
                    file.getOriginalFilename(), file.getSize(), idempotencyKey);

            CargaResultado resultado = cargarPedidosUseCase.cargarPedidos(file, idempotencyKey);

            log.info("Carga completada. Procesados: {}, Guardados: {}, Errores: {}",
                    resultado.getTotalProcesados(), resultado.getGuardados(), resultado.getConError());

            return ResponseEntity.ok(resultado);

        } finally {
            MDC.remove("correlationId");
        }
    }
}