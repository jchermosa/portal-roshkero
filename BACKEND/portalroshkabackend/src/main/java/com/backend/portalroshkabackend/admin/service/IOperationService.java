package com.backend.portalroshkabackend.admin.service;

import java.util.List;

import com.backend.portalroshkabackend.common.model.Equipos;
import com.backend.portalroshkabackend.common.model.Roles;
import com.backend.portalroshkabackend.common.model.Cargos;
import com.backend.portalroshkabackend.common.model.Request;

public interface IOperationService {
    List<Request> getAllRequests();

    List<Roles> getAllRols();

    List<Cargos> getAllCargos();

    List<Equipos> getAllTeams();

    Equipos postNewTeam(Equipos Equipos);

    void deleteTeam(int id_equipo);

    Equipos updateTeam(int id_equipo, Equipos equipoDetails);

}
