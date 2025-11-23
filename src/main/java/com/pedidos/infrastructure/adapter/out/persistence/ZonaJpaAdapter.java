package com.pedidos.infrastructure.adapter.out.persistence;

import com.pedidos.domain.model.Zona;
import com.pedidos.domain.port.out.ZonaRepository;
import com.pedidos.infrastructure.entity.ZonaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ZonaJpaAdapter implements ZonaRepository {

    private final ZonaJpaRepository jpaRepository;

    @Override
    public List<Zona> obtenerZonasPorIds(Set<String> ids) {
        return jpaRepository.findByIdIn(ids).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Zona toDomain(ZonaEntity entity) {
        return Zona.builder()
                .id(entity.getId())
                .soporteRefrigeracion(entity.isSoporteRefrigeracion())
                .build();
    }
}