package com.pedidos.domain.model;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    private String id;
    private boolean activo;
}