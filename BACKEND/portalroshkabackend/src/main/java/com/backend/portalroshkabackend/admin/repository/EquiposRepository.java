package com.backend.portalroshkabackend.admin.repository;

import com.backend.portalroshkabackend.common.model.Equipos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquiposRepository extends JpaRepository<Equipos, Integer> {
}
