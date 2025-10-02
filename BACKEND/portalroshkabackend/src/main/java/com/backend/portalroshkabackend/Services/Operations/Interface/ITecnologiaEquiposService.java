package com.backend.portalroshkabackend.Services.Operations.Interface;

import java.util.List;

import com.backend.portalroshkabackend.DTO.Operationes.TecnologiasDto;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;

public interface ITecnologiaEquiposService {
    Tecnologias getTecnologiaById(Integer id);
    List<TecnologiasDto> getTecnologiasByEquipo(Integer equipoId);
    List<TecnologiasDto>  updateTecnologiasEquipo(Equipos equipo, List<Integer> nuevasTecnologiasIds);
}