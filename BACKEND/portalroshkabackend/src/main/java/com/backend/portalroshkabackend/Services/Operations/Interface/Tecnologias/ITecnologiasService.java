package com.backend.portalroshkabackend.Services.Operations.Interface.Tecnologias;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasResponseDto;

public interface ITecnologiasService {
    Page<TecnologiasResponseDto> getAllTecnologias(Pageable pageable, String sortBy);

    TecnologiasResponseDto getTecnologiaById(Integer id);

    TecnologiasResponseDto createTecnologia(TecnologiasRequestDto dto);

    TecnologiasResponseDto updateTecnologia(Integer id, TecnologiasRequestDto dto);

    void deleteTecnologia(Integer id);
}