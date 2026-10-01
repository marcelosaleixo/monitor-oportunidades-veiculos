
-- Migration corretiva para garantir a existência
-- da tabela de anúncios no PostgreSQL.

CREATE TABLE IF NOT EXISTS anuncios (
    id BIGSERIAL PRIMARY KEY,

    fonte VARCHAR(50) NOT NULL,
    external_id VARCHAR(120) NOT NULL,

    marca VARCHAR(255),
    modelo VARCHAR(255),
    versao VARCHAR(255),

    ano_fabricacao INTEGER,
    ano_modelo INTEGER,
    quilometragem BIGINT,

    preco_anunciado NUMERIC(15,2),

    localidade VARCHAR(255),
    combustivel VARCHAR(255),
    motorizacao VARCHAR(255),

    imagem_url TEXT,
    anunciante VARCHAR(255),
    anunciante_codigo VARCHAR(255),
    anuncio_url TEXT,

    primeira_coleta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_coleta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_anuncio_fonte_external
        UNIQUE (fonte, external_id)
);

CREATE INDEX IF NOT EXISTS idx_anuncios_preco
    ON anuncios(preco_anunciado);

CREATE INDEX IF NOT EXISTS idx_anuncios_localidade
    ON anuncios(localidade);

CREATE INDEX IF NOT EXISTS idx_anuncios_modelo_ano
    ON anuncios(marca, modelo, ano_modelo);
