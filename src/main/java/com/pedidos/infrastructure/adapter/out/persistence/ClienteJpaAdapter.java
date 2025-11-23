package com.pedidos.infrastructure.adapter.out.persistence;

import com.pedidos.domain.model.Cliente;
import com.pedidos.domain.port.out.ClienteRepository;
import com.pedidos.infrastructure.entity.ClienteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClienteJpaAdapter implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;

    @Override
    public List<Cliente> obtenerClientesPorIds(Set<String> ids) {
        return jpaRepository.findByIdInAndActivoTrue(ids).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Cliente toDomain(ClienteEntity entity) {
        return Cliente.builder()
                .id(entity.getId())
                .activo(entity.isActivo())
                .build();
    }
}