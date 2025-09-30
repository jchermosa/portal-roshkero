package com.backend.portalroshkabackend.Repositories.UsuarioRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.TipoBeneficios;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;


@Repository
public interface TipoBeneficiosRepository extends JpaRepository<TipoBeneficios, Integer> {

    List<TipoBeneficios> findAllByOrderByNombreAsc();
    TipoBeneficios findByIdTipoBeneficio(Integer idTipoBeneficio);
    // List<TipoBeneficios> findAllEstadoActivo();
    List<TipoBeneficios> findByVigencia(EstadoActivoInactivo vigencia);
}