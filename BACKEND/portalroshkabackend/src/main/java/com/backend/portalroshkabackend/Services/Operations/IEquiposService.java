package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;

public interface IEquiposService {

    EquiposResponseDto postNewTeam(EquiposRequestDto equipo);

    void deleteTeam(int id_equipo);

    // EquiposResponseDto updateTeam(int id_equipo, EquiposRequestDto equipoDetails);

    Page<EquiposResponseDto> getTeamsSorted(Pageable pageable, String sortBy);
}
