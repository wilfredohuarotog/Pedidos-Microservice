package com.pedidos.domain.port.out;

import com.pedidos.domain.model.CargaIdempotencia;
import java.util.Optional;

public interface IdempotenciaRepository {
    Optional<CargaIdempotencia> buscarPorKeyYHash(String key, String hash);
    void guardar(CargaIdempotencia carga);
}