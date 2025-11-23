-- Datos de ejemplo para clientes
INSERT INTO clientes (id, activo) VALUES
('CLI-001', true),
('CLI-002', true),
('CLI-003', true),
('CLI-123', true),
('CLI-456', true),
('CLI-789', true),
('CLI-999', false);

-- Datos de ejemplo para zonas
INSERT INTO zonas (id, soporte_refrigeracion) VALUES
('ZONA1', true),
('ZONA2', true),
('ZONA3', false),
('ZONA4', false),
('ZONA5', true),
('NORTE', true),
('SUR', false),
('ESTE', true),
('OESTE', false),
('CENTRO', true);
