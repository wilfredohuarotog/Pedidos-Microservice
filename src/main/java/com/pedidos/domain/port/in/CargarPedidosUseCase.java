package com.pedidos.domain.port.in;

import com.pedidos.application.dto.CargaResultado;
import org.springframework.web.multipart.MultipartFile;

public interface CargarPedidosUseCase {
    CargaResultado cargarPedidos(MultipartFile archivo, String idempotencyKey);
}