package com.pedidos.domain.model;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    private UUID id;
    private String numeroPedido;
    private String clienteId;
    private String zonaId;
    private LocalDate fechaEntrega;
    private EstadoPedido estado;
    private boolean requiereRefrigeracion;

    public static Pedido crear(String numeroPedido, String clienteId, String zonaId,
                               LocalDate fechaEntrega, EstadoPedido estado, boolean requiereRefrigeracion) {
        return Pedido.builder()
                .id(UUID.randomUUID())
                .numeroPedido(numeroPedido)
                .clienteId(clienteId)
                .zonaId(zonaId)
                .fechaEntrega(fechaEntrega)
                .estado(estado)
                .requiereRefrigeracion(requiereRefrigeracion)
                .build();
    }
}
