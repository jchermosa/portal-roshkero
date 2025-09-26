package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDto;
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
    public List<UbicacionDto> getAllUbicacion() {
        List<Ubicacion> ubicaciones = ubicacionRepository.findAll();

        List<UbicacionDto> allUbicacion = ubicaciones.stream()
                .map(u -> new UbicacionDto(
                        u.getIdUbicacion(),
                        u.getNombre()
                ))
                .toList();

        return allUbicacion;
    }

        
}
