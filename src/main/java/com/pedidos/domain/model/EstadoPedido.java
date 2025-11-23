package com.pedidos.domain.model;

public enum EstadoPedido {
    PENDIENTE,
    CONFIRMADO,
    ENTREGADO;

    public static EstadoPedido fromString(String valor) {
        try {
            return EstadoPedido.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}