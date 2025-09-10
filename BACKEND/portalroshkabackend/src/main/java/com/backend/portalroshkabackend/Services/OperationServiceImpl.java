package com.backend.portalroshkabackend.Services;

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
                    Dto.setId_equipo(equipo.getIdEquipo());
                    Dto.setNombre(equipo.getNombre());
                    return Dto;
                });
    }

    @Override
    public EquiposResponseDto postNewTeam(EquiposRequestDto requestDto) {
        Equipos equipo = new Equipos();
        equipo.setNombre(requestDto.getNombre());

        Equipos saved = equiposRepository.save(equipo);

        EquiposResponseDto Dto = new EquiposResponseDto();
        Dto.setId_equipo(saved.getIdEquipo());
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
        Dto.setId_equipo(updated.getIdEquipo());
        Dto.setNombre(updated.getNombre());
        return Dto;
    }
}
