package com.marceloaleixo.monitor.config;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

/**
 * Adaptador isolado. O HAR mostra POST /api/resultado com JSON contendo
 * veiculo, order, page, size, filtros, keywords e fuzzyString. Cookies/tokens
 * não são persistidos aqui. Complete autenticação e payload somente após
 * validar acesso autorizado à fonte.
 */
@Component
public class FonteVeiculosClient {

    private final RestClient restClient;
    private final MonitorProperties properties;

    public FonteVeiculosClient(MonitorProperties properties) {
        // Cria o cliente diretamente para não depender de um bean RestClient.Builder.
        this.restClient = RestClient.create();
        this.properties = properties;
    }

    public JsonNode consultarPagina(int page, int size) {
        if (!properties.getFonte().isHabilitada()) {
            throw new IllegalStateException("Integração da fonte está desabilitada.");
        }
        var payload = new Consulta(page, size);
        return restClient.post().uri(properties.getFonte().getResultadoUrl())
                .header("Content-Type", "application/json").body(payload).retrieve().body(JsonNode.class);
    }

    public record Consulta(String veiculo, String order, int page, int size, Filtros filtros, String keywords, String fuzzyString) {

        public Consulta(int page, int size) {
            this("", "valor-asc", page, size, new Filtros(), "", "");
        }
    }

    public record Filtros(String ano_min, String ano_max, String valor_min, String valor_max, String km_min, String km_max) {

        public Filtros() {
            this("", "", "", "", "", "");
        }
    }
}
