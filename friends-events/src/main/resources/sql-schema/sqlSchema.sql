CREATE TABLE persona (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    canonical_id CHAR(64) NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    email_principal VARCHAR(200) NOT NULL,
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    fecha_nac DATE,               -- si decides calcular edad
    actualizado_en TIMESTAMP,     -- corresponde a updatedAt
    schema_version INT NOT NULL
);