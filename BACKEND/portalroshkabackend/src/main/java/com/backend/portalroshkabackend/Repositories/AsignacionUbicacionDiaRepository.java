package com.backend.portalroshkabackend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.portalroshkabackend.Models.AsignacionEquipoDiaUbicacion;

public interface AsignacionUbicacionDiaRepository extends JpaRepository<AsignacionEquipoDiaUbicacion, Integer> {

    @Query(value = """
            SELECT u.id_ubicacion, u.nombre AS ubicacion,
                   d.id_dia_laboral, d.nombre_dia AS dia
            FROM ubicacion u
            CROSS JOIN dias_laborales d
            WHERE NOT EXISTS (
                SELECT 1
                FROM asignacion_equipo_dia_ubicacion a
                WHERE a.id_ubicacion = u.id_ubicacion
                  AND a.id_dia_laboral = d.id_dia_laboral
            )
            """, nativeQuery = true)
    List<Object[]> findUbicacionesDiasLibresForEquipos();
}

