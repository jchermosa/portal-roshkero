package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.DiasLaboralDto;
import com.backend.portalroshkabackend.Models.DiaLaboral;
import com.backend.portalroshkabackend.Repositories.DiasLaboralRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IDiasLaboralesService;

@Service
public class DiasLaboralesServiceImpl implements IDiasLaboralesService{

    private final DiasLaboralRepository diasLaboralRepository;

    public DiasLaboralesServiceImpl(DiasLaboralRepository diasLaboralRepository) {
        this.diasLaboralRepository = diasLaboralRepository;
    }

    @Override
    public List<DiasLaboralDto> getAllDias() {
        List<DiaLaboral> diasLaboral = diasLaboralRepository.findAll();

        List<DiasLaboralDto> allDias = diasLaboral.stream()
                .map(u -> new DiasLaboralDto(
                        u.getIdDiaLaboral(),
                        u.getNombreDia()))
                .toList();

        return allDias;
    }
}
