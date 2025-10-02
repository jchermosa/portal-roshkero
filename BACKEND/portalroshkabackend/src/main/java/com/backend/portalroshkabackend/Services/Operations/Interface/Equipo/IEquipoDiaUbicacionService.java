package com.backend.portalroshkabackend.Services.Operations.Interface.Equipo;

import java.util.List;

import com.backend.portalroshkabackend.DTO.Operationes.EquipoDiaUbicacionResponceDto;

public interface IEquipoDiaUbicacionService {

    List<EquipoDiaUbicacionResponceDto> EquipoDiaUbicacion(Integer idEquipo);
}