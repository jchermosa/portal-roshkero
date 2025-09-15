package com.backend.portalroshkabackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.portalroshkabackend.Models.tipoDispositivo;

@Repository
public interface DispositivoRepository extends JpaRepository<tipoDispositivo, Integer> {

}
