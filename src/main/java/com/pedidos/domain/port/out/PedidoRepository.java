package com.pedidos.domain.port.out;

import com.pedidos.domain.model.Pedido;
import java.util.List;
import java.util.Set;

public interface PedidoRepository {
    void guardarLote(List<Pedido> pedidos);
    Set<String> obtenerNumerosPedidosExistentes(Set<String> numeros);
}