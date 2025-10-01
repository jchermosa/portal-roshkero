package com.backend.portalroshkabackend.Services.Operations.Interface;

import java.util.List;

import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;

public interface ITecnologiaService {
    Tecnologias getTecnologiaById(Integer id);

    void updateTecnologiasEquipo(Equipos equipo, List<Integer> nuevasTecnologiasIds);
}