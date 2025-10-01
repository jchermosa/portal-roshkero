package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Models.TecnologiasEquipos;
import com.backend.portalroshkabackend.Repositories.OP.TecnologiaRepository;
import com.backend.portalroshkabackend.Repositories.OP.TecnologiasEquiposRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.ITecnologiaService;

@Service
public class TecnologiasEquiposServiceImpl implements ITecnologiaService {

    private final TecnologiaRepository tecnologiaRepository;
    private final TecnologiasEquiposRepository tecnologiasEquiposRepository;

    @Autowired
    public TecnologiasEquiposServiceImpl(TecnologiaRepository tecnologiaRepository,
                                 TecnologiasEquiposRepository tecnologiasEquiposRepository) {
        this.tecnologiaRepository = tecnologiaRepository;
        this.tecnologiasEquiposRepository = tecnologiasEquiposRepository;
    }

    @Override
    public Tecnologias getTecnologiaById(Integer id) {
        return tecnologiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tecnologia not found: " + id));
    }

    @Override
    public void updateTecnologiasEquipo(Equipos equipo, List<Integer> nuevasTecnologiasIds) {
        List<TecnologiasEquipos> actuales = tecnologiasEquiposRepository.findAllByEquipo_IdEquipo(equipo.getIdEquipo());
        Set<Integer> actualesIds = actuales.stream()
                .map(te -> te.getTecnologia().getIdTecnologia())
                .collect(Collectors.toSet());
        Set<Integer> nuevasIds = new HashSet<>(nuevasTecnologiasIds);

        // delete not needed
        for (TecnologiasEquipos te : actuales) {
            if (!nuevasIds.contains(te.getTecnologia().getIdTecnologia())) {
                tecnologiasEquiposRepository.delete(te);
            }
        }

        // add new
        for (Integer idTec : nuevasTecnologiasIds) {
            if (!actualesIds.contains(idTec)) {
                Tecnologias tecnologia = getTecnologiaById(idTec);
                TecnologiasEquipos tecEquipo = new TecnologiasEquipos();
                tecEquipo.setEquipo(equipo);
                tecEquipo.setTecnologia(tecnologia);
                tecnologiasEquiposRepository.save(tecEquipo);
            }
        }
    }
}

