package com.pedidos.application.dto;

import lombok.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetalle {
    private int linea;
    private String numeroPedido;
    private List<String> motivos;
}