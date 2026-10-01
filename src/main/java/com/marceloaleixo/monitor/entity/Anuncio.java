package com.marceloaleixo.monitor.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="anuncios", uniqueConstraints=@UniqueConstraint(name="uk_anuncio_fonte_external", columnNames={"fonte","external_id"}))
public class Anuncio {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=50) private String fonte;
 @Column(name="external_id",nullable=false,length=120) private String externalId;
 private String marca; private String modelo; private String versao;
 @Column(name="ano_fabricacao") private Integer anoFabricacao;
 @Column(name="ano_modelo") private Integer anoModelo;
 private Long quilometragem;
 @Column(name="preco_anunciado",precision=15,scale=2) private BigDecimal precoAnunciado;
 private String localidade; private String combustivel; private String motorizacao;
 @Column(name="imagem_url") private String imagemUrl; private String anunciante;
 @Column(name="anunciante_codigo") private String anuncianteCodigo;
 @Column(name="anuncio_url") private String anuncioUrl;
 @Column(name="primeira_coleta",nullable=false) private LocalDateTime primeiraColeta=LocalDateTime.now();
 @Column(name="ultima_coleta",nullable=false) private LocalDateTime ultimaColeta=LocalDateTime.now();
 @Column(nullable=false) private boolean ativo=true;
 protected Anuncio() {}
 public Anuncio(String fonte,String externalId){this.fonte=fonte;this.externalId=externalId;}
 public Long getId(){return id;} public String getFonte(){return fonte;} public String getExternalId(){return externalId;}
 public String getMarca(){return marca;} public void setMarca(String v){marca=v;}
 public String getModelo(){return modelo;} public void setModelo(String v){modelo=v;}
 public String getVersao(){return versao;} public void setVersao(String v){versao=v;}
 public Integer getAnoFabricacao(){return anoFabricacao;} public void setAnoFabricacao(Integer v){anoFabricacao=v;}
 public Integer getAnoModelo(){return anoModelo;} public void setAnoModelo(Integer v){anoModelo=v;}
 public Long getQuilometragem(){return quilometragem;} public void setQuilometragem(Long v){quilometragem=v;}
 public BigDecimal getPrecoAnunciado(){return precoAnunciado;} public void setPrecoAnunciado(BigDecimal v){precoAnunciado=v;}
 public String getLocalidade(){return localidade;} public void setLocalidade(String v){localidade=v;}
 public String getCombustivel(){return combustivel;} public void setCombustivel(String v){combustivel=v;}
 public String getMotorizacao(){return motorizacao;} public void setMotorizacao(String v){motorizacao=v;}
 public String getImagemUrl(){return imagemUrl;} public void setImagemUrl(String v){imagemUrl=v;}
 public String getAnunciante(){return anunciante;} public void setAnunciante(String v){anunciante=v;}
 public String getAnuncianteCodigo(){return anuncianteCodigo;} public void setAnuncianteCodigo(String v){anuncianteCodigo=v;}
 public String getAnuncioUrl(){return anuncioUrl;} public void setAnuncioUrl(String v){anuncioUrl=v;}
 public LocalDateTime getPrimeiraColeta(){return primeiraColeta;} public LocalDateTime getUltimaColeta(){return ultimaColeta;}
 public boolean isAtivo(){return ativo;} public void marcarVisto(){ultimaColeta=LocalDateTime.now();ativo=true;}
 public void setFonte(String v){fonte=v;} public void setExternalId(String v){externalId=v;}
 public void setPrimeiraColeta(LocalDateTime v){primeiraColeta=v;} public void setUltimaColeta(LocalDateTime v){ultimaColeta=v;}
}
