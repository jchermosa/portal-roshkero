package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.CargosResponseDto;

public interface IPositionsService {
    Page<CargosResponseDto> getAllCargos(Pageable pageable);
}
