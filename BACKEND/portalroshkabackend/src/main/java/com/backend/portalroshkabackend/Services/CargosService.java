package com.backend.portalroshkabackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Repositories.CargosRepository;
import com.backend.portalroshkabackend.DTO.CargosResponseDto;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CargosService {

    @Autowired
    private CargosRepository cargoRepository;

    public List<CargosResponseDto> getAllCargos() {
        List<Cargos> cargos = (List<Cargos>) cargoRepository.findAll();
        return cargos.stream().map(this::mapToCargosResponseDto).collect(Collectors.toList());
    }
    
    private CargosResponseDto mapToCargosResponseDto(Cargos cargo) {
        CargosResponseDto dto = new CargosResponseDto();
        dto.setIdCargo(cargo.getIdCargo());
        dto.setNombre(cargo.getNombre());
        return dto;
    }
}
