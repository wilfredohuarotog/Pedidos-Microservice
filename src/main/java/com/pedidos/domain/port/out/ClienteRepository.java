package com.pedidos.domain.port.out;

import com.pedidos.domain.model.Cliente;
import java.util.List;
import java.util.Set;

public interface ClienteRepository {
    List<Cliente> obtenerClientesPorIds(Set<String> ids);
}