package com.backend.portalroshkabackend.Interfaces;


import org.springframework.data.domain.Page;
import com.backend.portalroshkabackend.DTO.CargosResponseDto;
import com.backend.portalroshkabackend.DTO.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.RequestResponseDto;
import com.backend.portalroshkabackend.DTO.RolesResponseDto;
import org.springframework.data.domain.Pageable;

public interface IOperationService {
    Page<RequestResponseDto> getAllRequests(Pageable pageable);

    Page<RolesResponseDto> getAllRols(Pageable pageable);

    Page<CargosResponseDto> getAllCargos(Pageable pageable);

    Page<EquiposResponseDto> getAllTeams(Pageable pageable);

    EquiposResponseDto postNewTeam(EquiposRequestDto equipo);

    void deleteTeam(int id_equipo);

    EquiposResponseDto updateTeam(int id_equipo, EquiposRequestDto equipoDetails);
}
