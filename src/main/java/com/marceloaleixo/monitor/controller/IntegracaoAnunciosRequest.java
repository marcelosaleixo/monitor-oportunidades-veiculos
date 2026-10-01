package com.marceloaleixo.monitor.controller;

import java.math.BigDecimal;
import java.util.List;

public record IntegracaoAnunciosRequest(String fonte, List<AnuncioRecebido> anuncios) {
    public record AnuncioRecebido(
            String externalId,
            String marca,
            String modelo,
            String versao,
            Integer anoFabricacao,
            Integer anoModelo,
            Long quilometragem,
            BigDecimal precoAnunciado,
            String localidade,
            String combustivel,
            String motorizacao,
            String imagemUrl,
            String anunciante,
            String anuncianteCodigo,
            String anuncioUrl
    ) {}
}
