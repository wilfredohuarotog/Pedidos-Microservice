package com.pedidos.domain.port.out;

import com.pedidos.domain.model.Zona;
import java.util.List;
import java.util.Set;

public interface ZonaRepository {
    List<Zona> obtenerZonasPorIds(Set<String> ids);
}