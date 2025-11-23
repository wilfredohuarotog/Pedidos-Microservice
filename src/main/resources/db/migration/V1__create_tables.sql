CREATE TABLE clientes (
    id VARCHAR PRIMARY KEY,
    activo BOOLEAN NOT NULL
);

CREATE TABLE zonas (
    id VARCHAR PRIMARY KEY,
    soporte_refrigeracion BOOLEAN NOT NULL
);

CREATE TABLE pedidos (
    id UUID PRIMARY KEY,
    numero_pedido VARCHAR NOT NULL,
    cliente_id VARCHAR NOT NULL,
    zona_id VARCHAR NOT NULL,
    fecha_entrega DATE NOT NULL,
    estado VARCHAR NOT NULL CHECK (estado IN ('PENDIENTE','CONFIRMADO','ENTREGADO')),
    requiere_refrigeracion BOOLEAN NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_pedidos_numero_pedido UNIQUE (numero_pedido)
);

CREATE INDEX idx_pedidos_estado_fecha
    ON pedidos (estado, fecha_entrega);

CREATE TABLE cargas_idempotencia (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR NOT NULL,
    archivo_hash VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_idempotencia UNIQUE (idempotency_key, archivo_hash)
);
