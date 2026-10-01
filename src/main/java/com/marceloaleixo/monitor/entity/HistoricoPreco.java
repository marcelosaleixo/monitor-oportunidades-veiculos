package com.marceloaleixo.monitor.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="historico_precos")
public class HistoricoPreco {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="anuncio_id") private Anuncio anuncio;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal preco;
 @Column(name="coletado_em",nullable=false) private LocalDateTime coletadoEm=LocalDateTime.now();
 protected HistoricoPreco(){}
 public HistoricoPreco(Anuncio a,BigDecimal p){anuncio=a;preco=p;}
}
