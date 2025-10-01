package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.PositionDto;
import com.backend.portalroshkabackend.DTO.PositionInsertDto;
import com.backend.portalroshkabackend.DTO.PositionUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICargosService {
    Page<PositionDto> getAllPositions(Pageable pageable);
    PositionDto getPositionById(int id);
    PositionDto addPosition(PositionInsertDto positionInsertDto);
    PositionDto updatePosition(PositionUpdateDto positionUpdateDto, int id);
    PositionDto deletePosition(int id);
}
