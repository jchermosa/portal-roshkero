package com.backend.portalroshkabackend.admin.service;

import java.util.List;

import com.backend.portalroshkabackend.admin.dto.EquipoRequestDto;
import com.backend.portalroshkabackend.admin.dto.EquipoResponseDto;
import com.backend.portalroshkabackend.common.model.Roles;
import com.backend.portalroshkabackend.common.model.Cargos;
import com.backend.portalroshkabackend.common.model.Request;

public interface IOperationService {
    List<Request> getAllRequests();

    List<Roles> getAllRols();

    List<Cargos> getAllCargos();

    List<EquipoResponseDto> getAllTeams();

    EquipoResponseDto postNewTeam(EquipoRequestDto equipo);

    void deleteTeam(int id_equipo);

    EquipoResponseDto updateTeam(int id_equipo, EquipoRequestDto equipoDetails);
}
