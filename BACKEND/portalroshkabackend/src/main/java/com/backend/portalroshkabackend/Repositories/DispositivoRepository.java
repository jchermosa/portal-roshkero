package com.backend.portalroshkabackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.portalroshkabackend.Models.Dispositivo;

@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Integer> {

}
