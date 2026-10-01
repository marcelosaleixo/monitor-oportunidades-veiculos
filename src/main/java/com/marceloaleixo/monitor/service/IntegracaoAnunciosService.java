package com.marceloaleixo.monitor.service;

import com.marceloaleixo.monitor.controller.IntegracaoAnunciosRequest;
import com.marceloaleixo.monitor.entity.Anuncio;
import com.marceloaleixo.monitor.entity.HistoricoPreco;
import com.marceloaleixo.monitor.repository.AnuncioRepository;
import com.marceloaleixo.monitor.repository.HistoricoPrecoRepository;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IntegracaoAnunciosService {
    private final AnuncioRepository anuncios;
    private final HistoricoPrecoRepository historicos;

    public IntegracaoAnunciosService(AnuncioRepository anuncios, HistoricoPrecoRepository historicos) {
        this.anuncios = anuncios;
        this.historicos = historicos;
    }

    @Transactional
    public Resultado importar(IntegracaoAnunciosRequest request) {
        if (request == null || request.fonte() == null || request.fonte().isBlank()) {
            throw new IllegalArgumentException("O campo 'fonte' é obrigatório.");
        }
        if (request.anuncios() == null) {
            throw new IllegalArgumentException("A lista 'anuncios' é obrigatória.");
        }

        String fonte = request.fonte().trim().toUpperCase(Locale.ROOT);
        int recebidos = 0, novos = 0, alterados = 0, ignorados = 0;
        for (var item : request.anuncios()) {
            if (item == null || item.externalId() == null || item.externalId().isBlank()) {
                ignorados++;
                continue;
            }
            String externalId = item.externalId().trim();
            Anuncio anuncio = anuncios.findByFonteAndExternalId(fonte, externalId).orElse(null);
            boolean novo = anuncio == null;
            if (novo) anuncio = new Anuncio(fonte, externalId);

            var precoAnterior = anuncio.getPrecoAnunciado();
            anuncio.setMarca(item.marca());
            anuncio.setModelo(item.modelo());
            anuncio.setVersao(item.versao());
            anuncio.setAnoFabricacao(item.anoFabricacao());
            anuncio.setAnoModelo(item.anoModelo());
            anuncio.setQuilometragem(item.quilometragem());
            anuncio.setPrecoAnunciado(item.precoAnunciado());
            anuncio.setLocalidade(item.localidade());
            anuncio.setCombustivel(item.combustivel());
            anuncio.setMotorizacao(item.motorizacao());
            anuncio.setImagemUrl(item.imagemUrl());
            anuncio.setAnunciante(item.anunciante());
            anuncio.setAnuncianteCodigo(item.anuncianteCodigo());
            anuncio.setAnuncioUrl(item.anuncioUrl());
            anuncio.marcarVisto();
            anuncio = anuncios.save(anuncio);

            if (anuncio.getPrecoAnunciado() != null &&
                    (novo || !anuncio.getPrecoAnunciado().equals(precoAnterior))) {
                historicos.save(new HistoricoPreco(anuncio, anuncio.getPrecoAnunciado()));
                if (!novo) alterados++;
            }
            if (novo) novos++;
            recebidos++;
        }
        return new Resultado(recebidos, novos, alterados, ignorados);
    }

    public record Resultado(int processados, int novos, int precosAlterados, int ignorados) {}
}
