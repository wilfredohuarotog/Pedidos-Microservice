package com.pedidos.domain.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CargaIdempotencia {
    private UUID id;
    private String idempotencyKey;
    private String archivoHash;
    private LocalDateTime createdAt;

    public static CargaIdempotencia crear(String idempotencyKey, String archivoHash) {
        return CargaIdempotencia.builder()
                .id(UUID.randomUUID())
                .idempotencyKey(idempotencyKey)
                .archivoHash(archivoHash)
                .createdAt(LocalDateTime.now())
                .build();
    }
}