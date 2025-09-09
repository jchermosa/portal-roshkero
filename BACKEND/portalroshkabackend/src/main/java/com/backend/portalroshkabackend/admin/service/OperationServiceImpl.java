package com.backend.portalroshkabackend.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import com.backend.portalroshkabackend.admin.dto.EquipoRequestDto;
import com.backend.portalroshkabackend.admin.dto.EquipoResponseDto;
import com.backend.portalroshkabackend.common.model.Cargos;
import com.backend.portalroshkabackend.common.model.Equipos;
import com.backend.portalroshkabackend.common.model.Request;
import com.backend.portalroshkabackend.common.model.Roles;
import com.backend.portalroshkabackend.admin.repository.CargosRepository;
import com.backend.portalroshkabackend.admin.repository.EquiposRepository;
import com.backend.portalroshkabackend.admin.repository.RequestRepository;
import com.backend.portalroshkabackend.admin.repository.RolesRepository;
import org.springframework.stereotype.Service;

@Service
public class OperationServiceImpl implements IOperationService {

    private final RequestRepository requestRepository;
    private final RolesRepository rolesRepository;
    private final CargosRepository cargosRepository;
    private final EquiposRepository equiposRepository;

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
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public List<Roles> getAllRols() {
        return rolesRepository.findAll();
    }

    @Override
    public List<Cargos> getAllCargos() {
        return cargosRepository.findAll();
    }

    // ----------------- TEAMS с Dto -----------------
    @Override
    public List<EquipoResponseDto> getAllTeams() {
        return equiposRepository.findAll()
                .stream()
                .map(equipo -> {
                    EquipoResponseDto Dto = new EquipoResponseDto();
                    Dto.setId_equipo(equipo.getId_equipo());
                    Dto.setNombre(equipo.getNombre());
                    return Dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EquipoResponseDto postNewTeam(EquipoRequestDto requestDto) {
        Equipos equipo = new Equipos();
        equipo.setNombre(requestDto.getNombre());

        Equipos saved = equiposRepository.save(equipo);

        EquipoResponseDto Dto = new EquipoResponseDto();
        Dto.setId_equipo(saved.getId_equipo());
        Dto.setNombre(saved.getNombre());
        return Dto;
    }

    @Override
    public void deleteTeam(int id_equipo) {
        equiposRepository.deleteById(id_equipo);
    }

    @Override
    public EquipoResponseDto updateTeam(int id_equipo, EquipoRequestDto requestDto) {
        Equipos existingEquipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        existingEquipo.setNombre(requestDto.getNombre());
        Equipos updated = equiposRepository.save(existingEquipo);

        EquipoResponseDto Dto = new EquipoResponseDto();
        Dto.setId_equipo(updated.getId_equipo());
        Dto.setNombre(updated.getNombre());
        return Dto;
    }
}
