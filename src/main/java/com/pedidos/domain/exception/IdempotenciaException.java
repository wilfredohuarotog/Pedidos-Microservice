package com.pedidos.domain.exception;

public class IdempotenciaException extends RuntimeException {
    public IdempotenciaException(String message) {
        super(message);
    }
}