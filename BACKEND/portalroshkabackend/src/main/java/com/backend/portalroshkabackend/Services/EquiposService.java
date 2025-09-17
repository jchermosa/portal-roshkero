package com.backend.portalroshkabackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Repositories.EquiposRepository;

@Service
public class EquiposService {
    

    @Autowired
    private final EquiposRepository equiposRepository;

    EquiposService(EquiposRepository equiposRepository) {
        this.equiposRepository = equiposRepository;
    }

    public Iterable<Equipos> getAllEquipos() {
        return equiposRepository.findAll();
    }


}
