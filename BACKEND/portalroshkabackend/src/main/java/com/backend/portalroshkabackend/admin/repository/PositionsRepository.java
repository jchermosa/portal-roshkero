package com.backend.portalroshkabackend.admin.repository;

import com.backend.portalroshkabackend.common.model.Cargos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionsRepository extends JpaRepository<Cargos, Integer> {
}
