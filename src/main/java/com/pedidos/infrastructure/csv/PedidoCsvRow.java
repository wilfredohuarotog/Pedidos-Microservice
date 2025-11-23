package com.pedidos.infrastructure.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.pedidos.domain.model.EstadoPedido;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCsvRow {

    @CsvBindByName(column = "numeroPedido")
    private String numeroPedido;

    @CsvBindByName(column = "clienteId")
    private String clienteId;

    @CsvBindByName(column = "fechaEntrega")
    @CsvDate("yyyy-MM-dd")
    private LocalDate fechaEntrega;

    @CsvBindByName(column = "estado")
    private String estadoString;

    @CsvBindByName(column = "zonaEntrega")
    private String zonaEntrega;

    @CsvBindByName(column = "requiereRefrigeracion")
    private boolean requiereRefrigeracion;

    public EstadoPedido getEstado() {
        return EstadoPedido.fromString(estadoString);
    }
}