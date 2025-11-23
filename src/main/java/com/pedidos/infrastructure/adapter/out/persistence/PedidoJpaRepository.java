package com.pedidos.infrastructure.adapter.out.persistence;

import com.pedidos.infrastructure.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, UUID> {

    @Query("SELECT p.numeroPedido FROM PedidoEntity p WHERE p.numeroPedido IN :numeros")
    Set<String> findNumerosPedidosExistentes(@Param("numeros") Set<String> numeros);
}