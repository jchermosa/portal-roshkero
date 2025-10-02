package com.backend.portalroshkabackend.Services.Operations.Interface.Equipo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;

public interface IEquiposService {

    EquiposResponseDto getTeamById(Integer idEquipo);

    void postNewTeam(EquiposRequestDto equipo);

    void toggleEquipo(Integer idEquipo);

    void updateTeam(Integer idEquipo, EquiposRequestDto equipoDetails);

    Page<EquiposResponseDto> getTeamsSorted(Pageable pageable, String sortBy);
}
