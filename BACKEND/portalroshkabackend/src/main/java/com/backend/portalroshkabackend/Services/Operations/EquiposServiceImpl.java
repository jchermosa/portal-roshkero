package com.backend.portalroshkabackend.Services.Operations;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Repositories.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.ClientesRepository;

@Service("operationsEquiposService")
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
        equipo.setIdCliente(cliente);
        equipo.setEstado(EstadoActivoInactivo.valueOf(requestDto.getEstado()));;
        equipo.setFechaCreacion(new Date(System.currentTimeMillis()));

        Equipos savedEquipo = equiposRepository.save(equipo);

        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(savedEquipo.getIdEquipo());
        dto.setNombre(savedEquipo.getNombre());
        dto.setFechaInicio(savedEquipo.getFechaInicio());
        dto.setFechaLimite(savedEquipo.getFechaLimite());
        dto.setCliente(savedEquipo.getCliente());
        dto.setFechaCreacion(savedEquipo.getFechaCreacion());
        dto.setEstado(savedEquipo.getEstado());

        return dto;
    }

    @Override
    public EquiposResponseDto updateTeam(int id_equipo, EquiposRequestDto requestDto) {
        Equipos existingEquipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Clientes cliente = clientesRepository.findById(requestDto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente not found"));

        existingEquipo.setNombre(requestDto.getNombre());
        existingEquipo.setFechaInicio(requestDto.getFechaInicio());
        existingEquipo.setFechaLimite(requestDto.getFechaLimite());
        existingEquipo.setIdCliente(cliente);
        existingEquipo.setEstado(EstadoActivoInactivo.valueOf(requestDto.getEstado()));

        equiposRepository.save(existingEquipo);

        Equipos updatedEquipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found after update"));

        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(updatedEquipo.getIdEquipo());
        dto.setNombre(updatedEquipo.getNombre());
        dto.setFechaInicio(updatedEquipo.getFechaInicio());
        dto.setFechaLimite(updatedEquipo.getFechaLimite());
        dto.setCliente(updatedEquipo.getCliente());
        dto.setFechaCreacion(updatedEquipo.getFechaCreacion());
        dto.setEstado(updatedEquipo.getEstado());

        return dto;
    }

    @Override
    public void deleteTeam(int id_equipo) {
        Equipos equipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        equipo.setEstado(EstadoActivoInactivo.I); //
        equiposRepository.save(equipo);
    }
}
