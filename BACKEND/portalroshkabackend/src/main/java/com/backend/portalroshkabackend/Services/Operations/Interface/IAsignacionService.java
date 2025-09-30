package com.backend.portalroshkabackend.Services.Operations.Interface;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.DiaConUbicacionesDto;
import com.backend.portalroshkabackend.DTO.Operationes.DiaUbicacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquipoDiaUbicacionResponceDto;

public interface IAsignacionService {
    Page<AsignacionResponseDto> getAllAsignacion(Pageable pageable);

    List<DiaConUbicacionesDto> getDiasConUbicacionesLibres();

    // void asignarDiasUbicaciones(@PathVariable Integer idEquipo, @RequestBody
    // EquipoAsignacionUpdateDiasUbicacionesDto request);
    void asignarDiasUbicacionesEquipo(Integer idEquipo, List<DiaUbicacionDto> diasUbicacionesEquipo);
}
