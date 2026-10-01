package com.marceloaleixo.monitor.config;

import com.marceloaleixo.monitor.service.ColetaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.marceloaleixo.monitor.config.MonitorProperties;

@Component
public class ColetaScheduler {

    private final MonitorProperties properties;
    private final ColetaService service;

    public ColetaScheduler(MonitorProperties p, ColetaService s) {
        properties = p;
        service = s;
    }

    @Scheduled(fixedDelayString = "${monitor.coleta.intervalo-ms:3600000}", initialDelayString = "${monitor.coleta.initial-delay-ms:30000}")
    public void agendar() {
        if (properties.getColeta().isEnabled()) {
            service.executar();
        }
    }
}
