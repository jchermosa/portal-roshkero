package com.backend.portalroshkabackend.admin.service;

import java.util.List;

import com.backend.portalroshkabackend.admin.dto.EquiposRequestDto;
import com.backend.portalroshkabackend.admin.dto.CargosResponseDto;
import com.backend.portalroshkabackend.admin.dto.EquiposResponseDto;
import com.backend.portalroshkabackend.admin.dto.RequestResponseDto;
import com.backend.portalroshkabackend.admin.dto.RolesResponseDto;

public interface IOperationService {
    List<RequestResponseDto> getAllRequests();

    List<RolesResponseDto> getAllRols();

    List<CargosResponseDto> getAllCargos();

    List<EquiposResponseDto> getAllTeams();

    EquiposResponseDto postNewTeam(EquiposRequestDto equipo);

    void deleteTeam(int id_equipo);

    EquiposResponseDto updateTeam(int id_equipo, EquiposRequestDto equipoDetails);
}
