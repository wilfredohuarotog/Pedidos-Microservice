package com.pedidos.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "activo", nullable = false)
    private boolean activo;
}
