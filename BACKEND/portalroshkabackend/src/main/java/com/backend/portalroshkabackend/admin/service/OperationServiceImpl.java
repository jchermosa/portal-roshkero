package com.backend.portalroshkabackend.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import com.backend.portalroshkabackend.admin.dto.EquiposRequestDto;

import com.backend.portalroshkabackend.admin.dto.EquiposResponseDto;
import com.backend.portalroshkabackend.admin.dto.CargosResponseDto;
import com.backend.portalroshkabackend.admin.dto.RolesResponseDto;
import com.backend.portalroshkabackend.admin.dto.RequestResponseDto;

import com.backend.portalroshkabackend.common.model.Equipos;

import com.backend.portalroshkabackend.admin.repository.CargosRepository;
import com.backend.portalroshkabackend.admin.repository.EquiposRepository;
import com.backend.portalroshkabackend.admin.repository.RequestRepository;
import com.backend.portalroshkabackend.admin.repository.RolesRepository;

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
    public List<RequestResponseDto> getAllRequests() {
        return requestRepository.findAll()
                .stream()
                .map(request -> {
                    RequestResponseDto dto = new RequestResponseDto();
                    dto.setId_solicitud(request.getIdSolicitud());
                    dto.setFecha_inicio(request.getFechaInicio());
                    dto.setFecha_fin(request.getFechaFin());
                    dto.setEstado(request.isEstado());
                    dto.setId_usuario(request.getIdUsuario());
                    dto.setCantidad_dias(request.getCantidadDias());
                    dto.setNumero_aprobaciones(request.getNumeroAprobaciones());
                    dto.setComentario(request.getComentario());
                    dto.setRechazado(request.isRechazado());
                    return dto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<RolesResponseDto> getAllRols() {
        return rolesRepository.findAll()
                .stream()
                .map(roles -> {
                    RolesResponseDto Dto = new RolesResponseDto();
                    Dto.setId_role(roles.getId_role());
                    Dto.setNombre(roles.getNombre());
                    return Dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CargosResponseDto> getAllCargos() {
        return cargosRepository.findAll()
                .stream()
                .map(cargos -> {
                    CargosResponseDto Dto = new CargosResponseDto();
                    Dto.setId_cargo(cargos.getIdCargo());
                    Dto.setNombre(cargos.getNombre());
                    return Dto;
                })
                .collect(Collectors.toList());
    }

    // ----------------- TEAMS -----------------
    @Override
    public List<EquiposResponseDto> getAllTeams() {
        return equiposRepository.findAll()
                .stream()
                .map(equipo -> {
                    EquiposResponseDto Dto = new EquiposResponseDto();
                    Dto.setId_equipo(equipo.getId_equipo());
                    Dto.setNombre(equipo.getNombre());
                    return Dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EquiposResponseDto postNewTeam(EquiposRequestDto requestDto) {
        Equipos equipo = new Equipos();
        equipo.setNombre(requestDto.getNombre());

        Equipos saved = equiposRepository.save(equipo);

        EquiposResponseDto Dto = new EquiposResponseDto();
        Dto.setId_equipo(saved.getId_equipo());
        Dto.setNombre(saved.getNombre());
        return Dto;
    }

    @Override
    public void deleteTeam(int id_equipo) {
        equiposRepository.deleteById(id_equipo);
    }

    @Override
    public EquiposResponseDto updateTeam(int id_equipo, EquiposRequestDto requestDto) {
        Equipos existingEquipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        existingEquipo.setNombre(requestDto.getNombre());
        Equipos updated = equiposRepository.save(existingEquipo);

        EquiposResponseDto Dto = new EquiposResponseDto();
        Dto.setId_equipo(updated.getId_equipo());
        Dto.setNombre(updated.getNombre());
        return Dto;
    }
}
