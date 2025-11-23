package com.pedidos.infrastructure.adapter.out.persistence;

import com.pedidos.infrastructure.entity.CargaIdempotenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CargaIdempotenciaJpaRepository extends JpaRepository<CargaIdempotenciaEntity, UUID> {
    Optional<CargaIdempotenciaEntity> findByIdempotencyKeyAndArchivoHash(String key, String hash);
}