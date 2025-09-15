package com.backend.portalroshkabackend.Services;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Repositories.CargosRepository;

public class CargosService {

    @Autowired
    private CargosRepository cargoRepository;

    public Iterable<Cargos> getAllCargos() {
        return cargoRepository.findAll();
    }
    
}
