package com.marceloaleixo.monitor.service;

import tools.jackson.databind.JsonNode;
import com.marceloaleixo.monitor.config.FonteVeiculosClient;
import com.marceloaleixo.monitor.entity.Anuncio;
import com.marceloaleixo.monitor.entity.HistoricoPreco;
import com.marceloaleixo.monitor.repository.AnuncioRepository;
import com.marceloaleixo.monitor.repository.HistoricoPrecoRepository;
import java.math.BigDecimal;
import java.util.Iterator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColetaService {
 private static final String FONTE="USADOFACIL";
 private final FonteVeiculosClient client;
 private final AnuncioRepository anuncios;
 private final HistoricoPrecoRepository historicos;
 public ColetaService(FonteVeiculosClient c,AnuncioRepository a,HistoricoPrecoRepository h){client=c;anuncios=a;historicos=h;}

 @Transactional
 public Resultado executar(){
  int processados=0,novos=0,precosAtualizados=0;
  int page=0,size=20;
  while(page<10){
   JsonNode response=client.consultarPagina(page,size);
   JsonNode rows=extrairLista(response);
   if(rows==null || !rows.isArray()) throw new IllegalStateException("Resposta da fonte não contém uma lista 'dados'/'data' válida.");
   if(rows.isEmpty()) break;
   for(JsonNode row:rows){
    String ext=text(row,"idveiculo","id","codigo");
    if(ext==null || ext.isBlank()) continue;
    Anuncio a=anuncios.findByFonteAndExternalId(FONTE,ext).orElse(null);
    boolean isNew=a==null;
    if(isNew) a=new Anuncio(FONTE,ext);
    BigDecimal old=a.getPrecoAnunciado();
    preencher(a,row);
    a.marcarVisto();
    a=anuncios.save(a);
    if(isNew || (a.getPrecoAnunciado()!=null && !a.getPrecoAnunciado().equals(old))){
      if(a.getPrecoAnunciado()!=null) historicos.save(new HistoricoPreco(a,a.getPrecoAnunciado()));
      if(!isNew) precosAtualizados++;
    }
    if(isNew) novos++;
    processados++;
   }
   if(rows.size()<size) break;
   page++;
  }
  return new Resultado(processados,novos,precosAtualizados);
 }
 private JsonNode extrairLista(JsonNode n){
  if(n==null)return null;
  if(n.isArray())return n;
  for(String k:new String[]{"dados","data","results","content"}) if(n.has(k)&&n.get(k).isArray())return n.get(k);
  return null;
 }
 private void preencher(Anuncio a,JsonNode n){
  a.setMarca(text(n,"fabricante","marca")); a.setModelo(text(n,"nome_modelo","modelo"));
  a.setVersao(text(n,"modelo","versao")); a.setAnoFabricacao(integer(n,"ano_fabricacao"));
  a.setAnoModelo(integer(n,"ano_modelo")); a.setQuilometragem(longValue(n,"km","quilometragem"));
  a.setPrecoAnunciado(decimal(n,"valor","preco","preco_anunciado"));
  a.setLocalidade(text(n,"localidade","cidade")); a.setCombustivel(text(n,"combustivel"));
  a.setMotorizacao(text(n,"motorizacao")); a.setImagemUrl(text(n,"imagem","imagem_url"));
  a.setAnunciante(text(n,"proprietario","anunciante")); a.setAnuncianteCodigo(text(n,"anunciante_code","anunciante_codigo"));
  a.setAnuncioUrl(text(n,"anunciante_url","anuncio_url"));
 }
 private String text(JsonNode n,String...keys){for(String k:keys)if(n.hasNonNull(k))return n.get(k).asText();return null;}
 private Integer integer(JsonNode n,String k){try{String s=text(n,k);return s==null||s.isBlank()?null:Integer.valueOf(s);}catch(Exception e){return null;}}
 private Long longValue(JsonNode n,String...keys){try{String s=text(n,keys);return s==null||s.isBlank()?null:Long.valueOf(s.replaceAll("\\D",""));}catch(Exception e){return null;}}
 private BigDecimal decimal(JsonNode n,String...keys){try{String s=text(n,keys);if(s==null||s.isBlank())return null;s=s.replace("R$","").replace(" ","");if(s.contains(","))s=s.replace(".","").replace(",",".");return new BigDecimal(s);}catch(Exception e){return null;}}
 public record Resultado(int processados,int novos,int precosAtualizados){}
}
