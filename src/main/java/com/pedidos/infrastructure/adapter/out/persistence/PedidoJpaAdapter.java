package com.pedidos.infrastructure.adapter.out.persistence;

import com.pedidos.domain.model.Pedido;
import com.pedidos.domain.port.out.PedidoRepository;
import com.pedidos.infrastructure.entity.PedidoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoJpaAdapter implements PedidoRepository {

    private final PedidoJpaRepository jpaRepository;

    @Override
    public void guardarLote(List<Pedido> pedidos) {
        List<PedidoEntity> entities = pedidos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        jpaRepository.saveAll(entities);
        jpaRepository.flush();
    }

    @Override
    public Set<String> obtenerNumerosPedidosExistentes(Set<String> numeros) {
        return jpaRepository.findNumerosPedidosExistentes(numeros);
    }

    private PedidoEntity toEntity(Pedido pedido) {
        return PedidoEntity.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .clienteId(pedido.getClienteId())
                .zonaId(pedido.getZonaId())
                .fechaEntrega(pedido.getFechaEntrega())
                .estado(pedido.getEstado().name())
                .requiereRefrigeracion(pedido.isRequiereRefrigeracion())
                .build();
    }
}
