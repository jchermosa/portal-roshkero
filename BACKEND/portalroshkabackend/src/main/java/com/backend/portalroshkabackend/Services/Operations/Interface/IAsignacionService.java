package com.backend.portalroshkabackend.Services.Operations.Interface;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionConDiasDto;


public interface IAsignacionService {
    Page<AsignacionResponseDto> getAllAsignacion(Pageable pageable);

    List<UbicacionConDiasDto> getUbicacionesConDiasLibres();
}
