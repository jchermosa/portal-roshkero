package com.backend.portalroshkabackend.Repositories.TH;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.portalroshkabackend.Models.TipoDispositivo;

@Repository
public interface TipoDispositivoRepository extends JpaRepository<TipoDispositivo, Integer> {

}
