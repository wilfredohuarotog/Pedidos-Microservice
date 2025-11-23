package com.pedidos.infrastructure.adapter.in.rest;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private List<String> details;
    private String correlationId;
    private LocalDateTime timestamp;
}
