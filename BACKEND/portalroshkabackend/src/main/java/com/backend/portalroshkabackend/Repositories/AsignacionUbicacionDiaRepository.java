package com.backend.portalroshkabackend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDto;
import com.backend.portalroshkabackend.Models.AsignacionEquipoDiaUbicacion;

@Repository
public interface AsignacionUbicacionDiaRepository extends JpaRepository<AsignacionEquipoDiaUbicacion, Integer> {

    @Query("""
            SELECT new com.backend.portalroshkabackend.DTO.Operationes.UbicacionDto(
                u.idUbicacion,
                u.nombre,
                d.idDiaLaboral,
                d.nombreDia
            )
            FROM Ubicacion u
            CROSS JOIN DiaLaboral d
            WHERE NOT EXISTS (
                SELECT 1
                FROM AsignacionEquipoDiaUbicacion a
                WHERE a.ubicacion.idUbicacion = u.idUbicacion
                  AND a.diaLaboral.idDiaLaboral = d.idDiaLaboral
            )
            """)
    List<UbicacionDto> findUbicacionesDiasLibresForEquipos();
}