package com.backend.portalroshkabackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Repositories.EquiposRepository;
import com.backend.portalroshkabackend.DTO.EquiposResponseDto;

@Service
public class EquiposService {

    @Autowired
    private final EquiposRepository equiposRepository;

    EquiposService(EquiposRepository equiposRepository) {
        this.equiposRepository = equiposRepository;
    }

    public List<EquiposResponseDto> getAllEquipos() {
        List<Equipos> equipos = (List<Equipos>) equiposRepository.findAll();
        return equipos.stream().map(this::mapToEquiposResponseDto).collect(Collectors.toList());
    }
    
    private EquiposResponseDto mapToEquiposResponseDto(Equipos equipo) {
        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(equipo.getIdEquipo());
        dto.setNombre(equipo.getNombre());
        return dto;
    }
}
