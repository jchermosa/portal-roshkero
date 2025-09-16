package com.backend.portalroshkabackend.Services.Operations;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.EstadoEquipo;
import com.backend.portalroshkabackend.Models.EstadoSolicitud;
import com.backend.portalroshkabackend.Repositories.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.ClientesRepository;

@Service
public class EquiposServiceImpl implements IEquiposService {
    private final EquiposRepository equiposRepository;
    private final ClientesRepository clientesRepository;

    @Autowired
    public EquiposServiceImpl(EquiposRepository equiposRepository, ClientesRepository clientesRepository) {
        this.equiposRepository = equiposRepository;
        this.clientesRepository = clientesRepository;
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
                    // Dto.setIdCliente(equipo.getIdCliente());
                    Dto.setCliente(equipo.getCliente());
                    Dto.setFechaCreacion(equipo.getFechaCreacion());
                    Dto.setEstado(equipo.getEstado());
                    return Dto;
                });
    }

    @Override
    public EquiposResponseDto postNewTeam(EquiposRequestDto requestDto) {
        Clientes cliente = clientesRepository.findById(requestDto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente not found"));

        Equipos equipo = new Equipos();
        equipo.setNombre(requestDto.getNombre());
        equipo.setFechaInicio(requestDto.getFechaInicio());
        equipo.setFechaLimite(requestDto.getFechaLimite());
        equipo.setIdCliente(cliente.getIdCliente());
        equipo.setEstado(requestDto.getEstado());
        equipo.setFechaCreacion(LocalDateTime.now().withNano(0));

        equiposRepository.save(equipo);

        Equipos savedWithCliente = equiposRepository.findByIdWithCliente(equipo.getIdEquipo())
                .orElseThrow(() -> new RuntimeException("Team not found after save"));

        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(savedWithCliente.getIdEquipo());
        dto.setNombre(savedWithCliente.getNombre());
        dto.setFechaInicio(savedWithCliente.getFechaInicio());
        dto.setFechaLimite(savedWithCliente.getFechaLimite());
        dto.setCliente(savedWithCliente.getCliente()); 
        dto.setFechaCreacion(savedWithCliente.getFechaCreacion());
        dto.setEstado(savedWithCliente.getEstado());

        return dto;
    }

    @Override
    public EquiposResponseDto updateTeam(int id_equipo, EquiposRequestDto requestDto) {
        Equipos existingEquipo = equiposRepository.findByIdWithCliente(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        existingEquipo.setNombre(requestDto.getNombre());
        existingEquipo.setFechaInicio(requestDto.getFechaInicio());
        existingEquipo.setFechaLimite(requestDto.getFechaLimite());
        existingEquipo.setIdCliente(requestDto.getIdCliente());
        existingEquipo.setEstado(requestDto.getEstado());

        equiposRepository.save(existingEquipo);

        Equipos updatedWithCliente = equiposRepository.findByIdWithCliente(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found after update"));

        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(updatedWithCliente.getIdEquipo());
        dto.setNombre(updatedWithCliente.getNombre());
        dto.setFechaInicio(updatedWithCliente.getFechaInicio());
        dto.setFechaLimite(updatedWithCliente.getFechaLimite());
        dto.setCliente(updatedWithCliente.getCliente()); 
        dto.setFechaCreacion(updatedWithCliente.getFechaCreacion());
        dto.setEstado(updatedWithCliente.getEstado());

        return dto;
    }

    @Override
    public void deleteTeam(int id_equipo) {
        Equipos equipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        equipo.setEstado(EstadoEquipo.I); //
        equiposRepository.save(equipo);
    }
}
