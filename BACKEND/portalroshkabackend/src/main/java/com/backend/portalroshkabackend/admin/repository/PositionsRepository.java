package com.backend.portalroshkabackend.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.portalroshkabackend.common.model.Cargos;

@Repository
public interface PositionsRepository extends JpaRepository<Cargos, Integer> {
}
