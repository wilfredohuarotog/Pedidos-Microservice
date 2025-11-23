package com.pedidos.infrastructure.adapter.in.rest;

import com.pedidos.domain.exception.IdempotenciaException;
import com.pedidos.domain.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.error("Error de validación: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message(ex.getMessage())
                .details(List.of(ex.getMessage()))
                .correlationId(MDC.get("correlationId"))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IdempotenciaException.class)
    public ResponseEntity<ErrorResponse> handleIdempotenciaException(IdempotenciaException ex) {
        log.warn("Carga duplicada detectada: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .code("DUPLICATE_REQUEST")
                .message(ex.getMessage())
                .details(List.of("Esta carga ya fue procesada previamente"))
                .correlationId(MDC.get("correlationId"))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        log.error("Archivo demasiado grande: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .code("FILE_TOO_LARGE")
                .message("El archivo excede el tamaño máximo permitido")
                .details(List.of("Tamaño máximo permitido: 50MB"))
                .correlationId(MDC.get("correlationId"))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error inesperado", ex);

        ErrorResponse error = ErrorResponse.builder()
                .code("INTERNAL_ERROR")
                .message("Error interno del servidor")
                .details(List.of(ex.getMessage()))
                .correlationId(MDC.get("correlationId"))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}