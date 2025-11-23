package com.pedidos.domain.model;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Zona {
    private String id;
    private boolean soporteRefrigeracion;
}