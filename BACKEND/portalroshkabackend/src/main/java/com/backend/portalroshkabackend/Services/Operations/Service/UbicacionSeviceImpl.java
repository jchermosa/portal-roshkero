package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDiaDto;
import com.backend.portalroshkabackend.Models.Ubicacion;
import com.backend.portalroshkabackend.Repositories.UbicacionRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IUbicacionSevice;

@Service
public class UbicacionSeviceImpl implements IUbicacionSevice {

    private final UbicacionRepository ubicacionRepository;

    public UbicacionSeviceImpl(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public List<UbicacionDiaDto> getAllUbicacion() {
        List<Ubicacion> ubicaciones = ubicacionRepository.findAll();

        List<UbicacionDiaDto> allUbicacion = ubicaciones.stream()
                .map(u -> new UbicacionDiaDto(
                        u.getIdUbicacion(),
                        u.getNombre()
                ))
                .toList();

        return allUbicacion;
    }

        
}
