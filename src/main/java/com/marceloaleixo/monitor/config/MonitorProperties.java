package com.marceloaleixo.monitor.config;

import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "monitor")
public class MonitorProperties {

    private Coleta coleta = new Coleta();
    private Fonte fonte = new Fonte();
    private Oportunidade oportunidade = new Oportunidade();

    public Coleta getColeta() {
        return coleta;
    }

    public void setColeta(Coleta v) {
        coleta = v;
    }

    public Fonte getFonte() {
        return fonte;
    }

    public void setFonte(Fonte v) {
        fonte = v;
    }

    public Oportunidade getOportunidade() {
        return oportunidade;
    }

    public void setOportunidade(Oportunidade v) {
        oportunidade = v;
    }

    public static class Coleta {

        private boolean enabled;
        private long intervaloMs = 3600000;
        private long initialDelayMs = 30000;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean v) {
            enabled = v;
        }

        public long getIntervaloMs() {
            return intervaloMs;
        }

        public void setIntervaloMs(long v) {
            intervaloMs = v;
        }

        public long getInitialDelayMs() {
            return initialDelayMs;
        }

        public void setInitialDelayMs(long v) {
            initialDelayMs = v;
        }
    }

    public static class Fonte {

        private String resultadoUrl;
        private boolean habilitada;

        public String getResultadoUrl() {
            return resultadoUrl;
        }

        public void setResultadoUrl(String v) {
            resultadoUrl = v;
        }

        public boolean isHabilitada() {
            return habilitada;
        }

        public void setHabilitada(boolean v) {
            habilitada = v;
        }
    }

    public static class Oportunidade {

        private BigDecimal capital = new BigDecimal("80000.00");
        private BigDecimal lucroMinimo = new BigDecimal("2999.00");
        private BigDecimal custoOperacional = new BigDecimal("1500.00");

        public BigDecimal getCapital() {
            return capital;
        }

        public void setCapital(BigDecimal v) {
            capital = v;
        }

        public BigDecimal getLucroMinimo() {
            return lucroMinimo;
        }

        public void setLucroMinimo(BigDecimal v) {
            lucroMinimo = v;
        }

        public BigDecimal getCustoOperacional() {
            return custoOperacional;
        }

        public void setCustoOperacional(BigDecimal custoOperacional) {
            this.custoOperacional = custoOperacional;
        }
        
    }
}
