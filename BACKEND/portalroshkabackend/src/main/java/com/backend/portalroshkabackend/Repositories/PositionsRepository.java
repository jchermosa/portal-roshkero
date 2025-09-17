package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.Cargos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionsRepository extends JpaRepository<Cargos, Integer> {
}
