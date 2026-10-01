package com.marceloaleixo.monitor.repository;
import com.marceloaleixo.monitor.entity.Anuncio;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AnuncioRepository extends JpaRepository<Anuncio,Long>{
 Optional<Anuncio> findByFonteAndExternalId(String fonte,String externalId);
 List<Anuncio> findAllByOrderByUltimaColetaDesc();
}
