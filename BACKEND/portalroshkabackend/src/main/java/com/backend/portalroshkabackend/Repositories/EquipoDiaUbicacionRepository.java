package com.backend.portalroshkabackend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.EquipoDiaUbicacion;

@Repository
public interface EquipoDiaUbicacionRepository extends JpaRepository<EquipoDiaUbicacion, Integer> {
    List<EquipoDiaUbicacion> findAllByEquipo_IdEquipo(Integer id);
}
