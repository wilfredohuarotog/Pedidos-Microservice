package com.pedidos.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "zonas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZonaEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "soporte_refrigeracion", nullable = false)
    private boolean soporteRefrigeracion;
}