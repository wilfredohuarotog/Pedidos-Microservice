package com.pedidos.application.dto;

import lombok.*;
import java.util.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CargaResultado {
    private int totalProcesados;
    private int guardados;
    private int conError;
    private List<ErrorDetalle> erroresDetalle;
    private Map<String, Integer> erroresAgrupados;
}