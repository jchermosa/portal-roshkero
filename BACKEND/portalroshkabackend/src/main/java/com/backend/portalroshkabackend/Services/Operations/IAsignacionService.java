package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;

public interface IAsignacionService {
    Page<AsignacionResponseDto> getAllAsignacion(Pageable pageable);
}
