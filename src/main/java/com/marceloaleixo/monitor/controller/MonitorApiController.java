package com.marceloaleixo.monitor.controller;

import com.marceloaleixo.monitor.repository.AnuncioRepository;
import com.marceloaleixo.monitor.service.*;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class MonitorApiController {

    private final AnuncioRepository anuncios;
    private final OportunidadeService oportunidades;
    private final ColetaService coleta;
    private final IntegracaoAnunciosService integracao;
    @Value("${monitor.integracao.token:}")
    private String tokenIntegracao;

    public MonitorApiController(AnuncioRepository a, OportunidadeService o, ColetaService c, IntegracaoAnunciosService i) {
        anuncios = a;
        oportunidades = o;
        coleta = c;
        integracao = i;
    }

    @GetMapping("/anuncios")
    public Object anuncios() {
        return anuncios.findAllByOrderByUltimaColetaDesc();
    }

    @GetMapping("/oportunidades")
    public Object oportunidades() {
        return oportunidades.listar();
    }

    @PostMapping("/coleta/executar")
    public Object executar() {
        return coleta.executar();
    }

    @GetMapping("/monitor/status")
    public Map<String, Object> status() {
        return Map.of("status", "UP", "anuncios", anuncios.count(), "oportunidades", oportunidades.listar().size());
    }

    @PostMapping("/integracao/anuncios")
    public ResponseEntity<?> receberAnuncios(
            @RequestHeader(value = "X-Integration-Token", required = false) String token,
            @RequestBody IntegracaoAnunciosRequest request) {
        if (tokenIntegracao == null || tokenIntegracao.isBlank() || token == null || !tokenIntegracao.equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("erro", "Token de integração inválido ou não configurado."));
        }
        try {
            return ResponseEntity.ok(integracao.importar(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}
