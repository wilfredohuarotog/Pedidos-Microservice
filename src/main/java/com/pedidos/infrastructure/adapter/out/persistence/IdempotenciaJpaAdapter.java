package com.pedidos.infrastructure.adapter.out.persistence;

import com.pedidos.domain.model.CargaIdempotencia;
import com.pedidos.domain.port.out.IdempotenciaRepository;
import com.pedidos.infrastructure.entity.CargaIdempotenciaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotenciaJpaAdapter implements IdempotenciaRepository {

    private final CargaIdempotenciaJpaRepository jpaRepository;

    @Override
    public Optional<CargaIdempotencia> buscarPorKeyYHash(String key, String hash) {
        return jpaRepository.findByIdempotencyKeyAndArchivoHash(key, hash)
                .map(this::toDomain);
    }

    @Override
    public void guardar(CargaIdempotencia carga) {
        CargaIdempotenciaEntity entity = toEntity(carga);
        jpaRepository.save(entity);
    }

    private CargaIdempotencia toDomain(CargaIdempotenciaEntity entity) {
        return CargaIdempotencia.builder()
                .id(entity.getId())
                .idempotencyKey(entity.getIdempotencyKey())
                .archivoHash(entity.getArchivoHash())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private CargaIdempotenciaEntity toEntity(CargaIdempotencia carga) {
        return CargaIdempotenciaEntity.builder()
                .id(carga.getId())
                .idempotencyKey(carga.getIdempotencyKey())
                .archivoHash(carga.getArchivoHash())
                .createdAt(carga.getCreatedAt())
                .build();
    }
}