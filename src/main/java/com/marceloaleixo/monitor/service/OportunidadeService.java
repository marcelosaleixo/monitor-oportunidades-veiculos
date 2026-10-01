package com.marceloaleixo.monitor.service;

import com.marceloaleixo.monitor.config.MonitorProperties;
import com.marceloaleixo.monitor.entity.Anuncio;
import com.marceloaleixo.monitor.repository.AnuncioRepository;
import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class OportunidadeService {

    private final AnuncioRepository repo;
    private final MonitorProperties props;

    public OportunidadeService(AnuncioRepository r, MonitorProperties p) {
        repo = r;
        props = p;
    }

    public List<OportunidadeDTO> listar() {
        List<Anuncio> all = repo.findAll();
        List<OportunidadeDTO> out = new ArrayList<>();
        for (Anuncio a : all) {
            if (a.getPrecoAnunciado() == null || a.getPrecoAnunciado().compareTo(props.getOportunidade().getCapital()) > 0) {
                continue;
            }
            List<BigDecimal> comps = all.stream().filter(b -> b.getId() != null && !b.getId().equals(a.getId()))
                    .filter(b -> b.getPrecoAnunciado() != null && Objects.equals(norm(b.getMarca()), norm(a.getMarca())) && Objects.equals(norm(b.getModelo()), norm(a.getModelo())))
                    .filter(b -> a.getAnoModelo() == null || b.getAnoModelo() == null || Math.abs(a.getAnoModelo() - b.getAnoModelo()) <= 1)
                    .map(Anuncio::getPrecoAnunciado).sorted().toList();
            if (comps.size() < 2) {
                continue;
            }
            BigDecimal rev = comps.get(comps.size() / 2);
            BigDecimal custos = props.getOportunidade().getCustoOperacional();
            BigDecimal lucro = rev.subtract(a.getPrecoAnunciado()).subtract(custos);
            if (lucro.compareTo(props.getOportunidade().getLucroMinimo()) >= 0) {
                out.add(new OportunidadeDTO(a.getId(), a.getMarca(), a.getModelo(), a.getAnoModelo(), a.getLocalidade(), a.getPrecoAnunciado(), rev, custos, lucro, lucro.divide(a.getPrecoAnunciado(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")), a.getImagemUrl(), a.getAnuncioUrl(), comps.size()));
            }
        }
        out.sort(Comparator.comparing(OportunidadeDTO::lucroEstimado).reversed());
        return out;
    }

    private String norm(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    public record OportunidadeDTO(Long anuncioId, String marca, String modelo, Integer anoModelo, String localidade, BigDecimal precoCompra, BigDecimal revendaReferencia, BigDecimal custoOperacional, BigDecimal lucroEstimado, BigDecimal roiPercentual, String imagemUrl, String anuncioUrl, int comparaveis) {

    }
}
