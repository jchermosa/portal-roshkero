package com.backend.portalroshkabackend.Services;

import java.util.List;

import com.backend.portalroshkabackend.DTO.CargosResponseDto;
import com.backend.portalroshkabackend.DTO.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.RequestResponseDto;
import com.backend.portalroshkabackend.DTO.RolesResponseDto;

public interface IOperationService {
    List<RequestResponseDto> getAllRequests();

    List<RolesResponseDto> getAllRols();

    List<CargosResponseDto> getAllCargos();

    List<EquiposResponseDto> getAllTeams();

    EquiposResponseDto postNewTeam(EquiposRequestDto equipo);

    void deleteTeam(int id_equipo);

    EquiposResponseDto updateTeam(int id_equipo, EquiposRequestDto equipoDetails);
}
