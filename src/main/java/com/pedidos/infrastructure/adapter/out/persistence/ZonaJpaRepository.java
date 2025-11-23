package com.pedidos.infrastructure.adapter.out.persistence;

import com.pedidos.infrastructure.entity.ZonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ZonaJpaRepository extends JpaRepository<ZonaEntity, String> {
    List<ZonaEntity> findByIdIn(Set<String> ids);
}