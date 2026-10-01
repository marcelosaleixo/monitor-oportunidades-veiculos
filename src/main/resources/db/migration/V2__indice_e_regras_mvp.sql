CREATE INDEX IF NOT EXISTS idx_anuncios_ativos_preco ON anuncios(ativo, preco_anunciado);
CREATE INDEX IF NOT EXISTS idx_anuncios_comparaveis ON anuncios(lower(marca), lower(modelo), ano_modelo);
