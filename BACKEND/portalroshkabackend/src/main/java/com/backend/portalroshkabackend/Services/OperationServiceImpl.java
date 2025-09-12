package com.backend.portalroshkabackend.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.backend.portalroshkabackend.DTO.CargosResponseDto;
import com.backend.portalroshkabackend.DTO.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.RequestResponseDto;
import com.backend.portalroshkabackend.DTO.RolesResponseDto;
import com.backend.portalroshkabackend.Repositories.CargosRepository;
import com.backend.portalroshkabackend.Repositories.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.RequestRepository;
import com.backend.portalroshkabackend.Repositories.RolesRepository;
import com.backend.portalroshkabackend.Models.Equipos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationServiceImpl implements IOperationService {

    private final RequestRepository requestRepository;
    private final RolesRepository rolesRepository;
    private final CargosRepository cargosRepository;
    private final EquiposRepository equiposRepository;

    @Autowired
    public OperationServiceImpl(RequestRepository requestRepository,
            RolesRepository rolesRepository,
            CargosRepository cargosRepository,
            EquiposRepository equiposRepository) {
        this.requestRepository = requestRepository;
        this.rolesRepository = rolesRepository;
        this.cargosRepository = cargosRepository;
        this.equiposRepository = equiposRepository;
    }

    // ----------------- READ -----------------
    @Override
    public Page<RequestResponseDto> getAllRequests(Pageable pageable) {
        return requestRepository.findAll(pageable)
                .map(request -> {
                    RequestResponseDto dto = new RequestResponseDto();
                    dto.setIdSolicitud(request.getIdSolicitud());
                    dto.setFechaInicio(request.getFechaInicio());
                    dto.setFechaFin(request.getFechaFin());
                    dto.setIdUsuario(request.getIdUsuario());
                    dto.setCantidadDias(request.getCantidadDias());
                    dto.setNumeroAprobaciones(request.getNumeroAprobaciones());
                    dto.setComentario(request.getComentario());
                    dto.setidSolicitudTipo(request.getidSolicitudTipo());
                    dto.setEstado(request.getEstado());
                    dto.setFechaCreacion(request.getFechaCreacion());

                    return dto;
                });

    }

    @Override
    public Page<RolesResponseDto> getAllRols(Pageable pageable) {
        return rolesRepository.findAll(pageable)
                .map(roles -> {
                    RolesResponseDto Dto = new RolesResponseDto();
                    Dto.setId_role(roles.getIdRol());
                    Dto.setNombre(roles.getNombre());
                    return Dto;
                });
    }

    @Override
    public Page<CargosResponseDto> getAllCargos(Pageable pageable) {
        return cargosRepository.findAll(pageable)
                .map(cargos -> {
                    CargosResponseDto Dto = new CargosResponseDto();
                    Dto.setId_cargo(cargos.getIdCargo());
                    Dto.setNombre(cargos.getNombre());
                    return Dto;
                });
    }

    // ----------------- TEAMS -----------------
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
