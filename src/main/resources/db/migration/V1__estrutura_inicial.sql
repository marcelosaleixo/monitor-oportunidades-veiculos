CREATE TABLE configuracoes_monitor (
 id BIGSERIAL PRIMARY KEY, regiao VARCHAR(100) NOT NULL DEFAULT 'MT',
 capital_disponivel NUMERIC(15,2) NOT NULL DEFAULT 80000.00,
 lucro_minimo NUMERIC(15,2) NOT NULL DEFAULT 2999.00,
 intervalo_minutos INTEGER NOT NULL DEFAULT 60, ativo BOOLEAN NOT NULL DEFAULT TRUE,
 criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO configuracoes_monitor(regiao,capital_disponivel,lucro_minimo,intervalo_minutos)
SELECT 'MT',80000.00,2999.00,60 WHERE NOT EXISTS (SELECT 1 FROM configuracoes_monitor);

CREATE TABLE anuncios (
 id BIGSERIAL PRIMARY KEY, fonte VARCHAR(50) NOT NULL, external_id VARCHAR(120) NOT NULL,
 marca VARCHAR(100), modelo VARCHAR(150), versao VARCHAR(200), ano_fabricacao INTEGER,
 ano_modelo INTEGER, quilometragem BIGINT, preco_anunciado NUMERIC(15,2),
 localidade VARCHAR(150), combustivel VARCHAR(50), motorizacao VARCHAR(50),
 imagem_url TEXT, anunciante VARCHAR(200), anunciante_codigo VARCHAR(100), anuncio_url TEXT,
 primeira_coleta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 ultima_coleta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, ativo BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT uk_anuncio_fonte_external UNIQUE(fonte,external_id)
);
CREATE INDEX idx_anuncios_preco ON anuncios(preco_anunciado);
CREATE INDEX idx_anuncios_localidade ON anuncios(localidade);
CREATE INDEX idx_anuncios_modelo_ano ON anuncios(marca,modelo,ano_modelo);

CREATE TABLE historico_precos (
 id BIGSERIAL PRIMARY KEY, anuncio_id BIGINT NOT NULL REFERENCES anuncios(id),
 preco NUMERIC(15,2) NOT NULL, coletado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_historico_anuncio_data ON historico_precos(anuncio_id,coletado_em DESC);

CREATE TABLE oportunidades (
 id BIGSERIAL PRIMARY KEY, anuncio_id BIGINT NOT NULL REFERENCES anuncios(id),
 preco_referencia NUMERIC(15,2), custo_aquisicao NUMERIC(15,2), custo_operacional NUMERIC(15,2),
 preco_revenda_estimado NUMERIC(15,2), lucro_estimado NUMERIC(15,2), roi NUMERIC(8,2),
 classificacao VARCHAR(30), calculado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE execucoes_coleta (
 id BIGSERIAL PRIMARY KEY, inicio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, fim TIMESTAMP,
 status VARCHAR(30) NOT NULL, anuncios_processados INTEGER NOT NULL DEFAULT 0,
 novos_anuncios INTEGER NOT NULL DEFAULT 0, erros INTEGER NOT NULL DEFAULT 0, mensagem_erro TEXT
);
CREATE TABLE notificacoes (
 id BIGSERIAL PRIMARY KEY, oportunidade_id BIGINT REFERENCES oportunidades(id),
 canal VARCHAR(30) NOT NULL, destinatario VARCHAR(50), mensagem TEXT NOT NULL,
 status VARCHAR(30) NOT NULL DEFAULT 'PENDENTE', enviada_em TIMESTAMP, erro TEXT
);
