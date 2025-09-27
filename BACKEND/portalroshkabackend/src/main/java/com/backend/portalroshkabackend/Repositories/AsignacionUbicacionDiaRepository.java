package com.backend.portalroshkabackend.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDiaDto;
import com.backend.portalroshkabackend.Models.DiaLaboral;
import com.backend.portalroshkabackend.Models.EquipoDiaUbicacion;
import com.backend.portalroshkabackend.Models.Equipos;

@Repository
public interface AsignacionUbicacionDiaRepository extends JpaRepository<EquipoDiaUbicacion, Integer> {

    Optional<EquipoDiaUbicacion> findByEquipoAndDiaLaboral(Equipos equipo, DiaLaboral dia);

    @Query("""
            SELECT new com.backend.portalroshkabackend.DTO.Operationes.UbicacionDiaDto(
                u.idUbicacion,
                u.nombre,
                dl.idDiaLaboral,
                dl.nombreDia
            )
            FROM DiaLaboral dl
            CROSS JOIN Ubicacion u
            LEFT JOIN EquipoDiaUbicacion a
                ON a.diaLaboral = dl
                AND a.ubicacion = u
            WHERE a.id IS NULL
            ORDER BY dl.idDiaLaboral, u.idUbicacion
            """)
    List<UbicacionDiaDto> findUbicacionesDiasLibresForEquipos();
}
