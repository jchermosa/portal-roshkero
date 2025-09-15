package com.backend.portalroshkabackend.Services.Operations;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.Models.Equipos;

import com.backend.portalroshkabackend.Repositories.EquiposRepository;

@Service
public class EquiposServiceImpl implements IEquiposService {
    private final EquiposRepository equiposRepository;

    @Autowired
    public EquiposServiceImpl(EquiposRepository equiposRepository) {
        this.equiposRepository = equiposRepository;
    }

    @Override
    public Page<EquiposResponseDto> getAllTeams(Pageable pageable) {
        return equiposRepository.findAll(pageable)
                .map(equipo -> {
                    EquiposResponseDto Dto = new EquiposResponseDto();
                    Dto.setIdEquipo(equipo.getIdEquipo());
                    Dto.setNombre(equipo.getNombre());
                    Dto.setFechaInicio(equipo.getFechaInicio());
                    Dto.setFechaLimite(equipo.getFechaLimite());
                    Dto.setIdCliente(equipo.getIdCliente());
                    Dto.setFechaCreacion(equipo.getFechaCreacion());
                    Dto.setEstado(equipo.isEstado());
                    return Dto;
                });
    }

    @Override
    public EquiposResponseDto postNewTeam(EquiposRequestDto requestDto) {
        Equipos equipo = new Equipos();
        equipo.setNombre(requestDto.getNombre());
        equipo.setFechaInicio(requestDto.getFechaInicio());
        equipo.setFechaLimite(requestDto.getFechaLimite());
        equipo.setIdCliente(requestDto.getIdCliente());
        equipo.setEstado(requestDto.isEstado());
        equipo.setFechaCreacion(LocalDateTime.now().withNano(0)); // new date

        Equipos saved = equiposRepository.save(equipo);

        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(saved.getIdEquipo());
        dto.setNombre(saved.getNombre());
        dto.setFechaInicio(saved.getFechaInicio());
        dto.setFechaLimite(saved.getFechaLimite());
        dto.setIdCliente(saved.getIdCliente());
        dto.setFechaCreacion(saved.getFechaCreacion());
        dto.setEstado(saved.isEstado());

        return dto;
    }

    @Override
    public void deleteTeam(int id_equipo) {
        Equipos equipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        equipo.setEstado(false); //
        equiposRepository.save(equipo);
    }

    @Override
    public EquiposResponseDto updateTeam(int id_equipo, EquiposRequestDto requestDto) {

        Equipos existingEquipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        existingEquipo.setNombre(requestDto.getNombre());
        existingEquipo.setFechaInicio(requestDto.getFechaInicio());
        existingEquipo.setFechaLimite(requestDto.getFechaLimite());
        existingEquipo.setIdCliente(requestDto.getIdCliente());
        existingEquipo.setEstado(requestDto.isEstado());

        Equipos updated = equiposRepository.save(existingEquipo);

        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(updated.getIdEquipo());
        dto.setNombre(updated.getNombre());
        dto.setFechaInicio(updated.getFechaInicio());
        dto.setFechaLimite(updated.getFechaLimite());
        dto.setIdCliente(updated.getIdCliente());
        dto.setFechaCreacion(updated.getFechaCreacion()); // not new. take from form
        dto.setEstado(updated.isEstado());

        return dto;
    }
}
