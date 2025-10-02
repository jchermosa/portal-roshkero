package com.backend.portalroshkabackend.Services.Operations.Interface.Equipo;

import java.util.List;

import com.backend.portalroshkabackend.DTO.Operationes.EquipoDiaUbicacionResponceDto;

public interface IEquipoUbicacionService {
    List<EquipoDiaUbicacionResponceDto> getUbicacionesByEquipo(Integer equipoId);
}
