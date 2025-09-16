package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.CargosResponseDto;
import com.backend.portalroshkabackend.Repositories.CargosRepository;

@Service("operationsPositionsService")
public class PositionsServiceImpl implements IPositionsService {

    private final CargosRepository cargosRepository;

    @Autowired
    public PositionsServiceImpl(CargosRepository cargosRepository) {
        this.cargosRepository = cargosRepository;
    }

    @Override
    public Page<CargosResponseDto> getAllCargos(Pageable pageable) {
        return cargosRepository.findAll(pageable)
                .map(cargos -> {
                    CargosResponseDto Dto = new CargosResponseDto();
                    Dto.setIdCargo(cargos.getIdCargo());
                    Dto.setNombre(cargos.getNombre());
                    return Dto;
                });
    }
}
