package com.backend.portalroshkabackend.Services.Operations.Interface;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.DiaConUbicacionesDto;
import com.backend.portalroshkabackend.DTO.Operationes.DiaUbicacionDto;

public interface IAsignacionService {
    Page<AsignacionResponseDto> getAllAsignacion(Pageable pageable);

    List<DiaConUbicacionesDto> getDiasConUbicacionesLibres();

    void asignarDiasUbicacionesEquipo(Integer idEquipo, List<DiaUbicacionDto> diasUbicacionesEquipo);

    void toggleEquipoEstado(Integer idEquipo);
}
