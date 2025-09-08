package com.backend.portalroshkabackend.admin.service;

import java.util.List;

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

    @Override
    public List<Equipos> getAllTeams() {
        return equiposRepository.findAll();
    }

    @Override
    public Equipos postNewTeam(Equipos equipo) {
        return equiposRepository.save(equipo);
    }

    @Override
    public void deleteTeam(int id_equipo) {
        equiposRepository.deleteById(id_equipo);
    }

    @Override
    public Equipos updateTeam(int id_equipo, Equipos equipoDetails) {
        Equipos existingEquipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        existingEquipo.setNombre(equipoDetails.getNombre());
        return equiposRepository.save(existingEquipo);
    }

}
