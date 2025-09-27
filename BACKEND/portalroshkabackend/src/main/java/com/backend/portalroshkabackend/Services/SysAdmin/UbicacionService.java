package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.SYSADMIN.UbicacionDto;
import com.backend.portalroshkabackend.Models.Ubicacion;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.UbicacionCRUDRepository;

import jakarta.transaction.Transactional;

@Service
public class UbicacionService {


    @Autowired 
    private final UbicacionCRUDRepository ubicacionRepository;

    public UbicacionService(UbicacionCRUDRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }


    // Encontrar ubicacion por ID

    @Transactional
    public Optional<UbicacionDto> findByIdUbicacion(Integer idUbicacion){
        return ubicacionRepository.findByIdUbicacion(idUbicacion)
                .map(ubicacion -> {
                    UbicacionDto dto = new UbicacionDto();
                    dto.setIdUbicacion(ubicacion.getIdUbicacion());
                    dto.setNombre(ubicacion.getNombre());
                    dto.setEstado(ubicacion.getEstado());
                    
                    
                    return dto;
                });
    }


    @Transactional
    // Encontrar todas las ubicaciones
    public List<UbicacionDto> getAllUbicaciones(){
        List<com.backend.portalroshkabackend.Models.Ubicacion> ubicaciones = ubicacionRepository.findAll();
        return ubicaciones.stream()
                .map(ubicacion -> {
                    UbicacionDto dto = new UbicacionDto();
                    dto.setIdUbicacion(ubicacion.getIdUbicacion());
                    dto.setNombre(ubicacion.getNombre());
                    dto.setEstado(ubicacion.getEstado());
                    
                    
                    return dto;
                })
                .toList();
    }
    

    // crear nueva ubicacion
    @Transactional
    public UbicacionDto createUbicacion(UbicacionDto ubicacionDto){
        com.backend.portalroshkabackend.Models.Ubicacion ubicacion = new com.backend.portalroshkabackend.Models.Ubicacion();
        ubicacion.setNombre(ubicacionDto.getNombre());
        ubicacion.setEstado(ubicacionDto.getEstado());
        ubicacionRepository.save(ubicacion);
        ubicacionDto.setIdUbicacion(ubicacion.getIdUbicacion());
        return ubicacionDto;
    }


    // actualizar ubicacion
    @Transactional
    public Optional<UbicacionDto> updateUbicacion(Integer idUbicacion, UbicacionDto ubicacionDto){
        return ubicacionRepository.findByIdUbicacion(idUbicacion)
                .map(ubicacion -> {
                    ubicacion.setNombre(ubicacionDto.getNombre());
                    ubicacion.setEstado(ubicacionDto.getEstado());
                    ubicacionRepository.save(ubicacion);
                    ubicacionDto.setIdUbicacion(ubicacion.getIdUbicacion());
                    return ubicacionDto;
                });
    }

    // eliminar ubicacion
    @Transactional
    public void deleteUbicacion(Integer idUbicacion){
        Ubicacion ubicacion = ubicacionRepository.findByIdUbicacion(idUbicacion)
            .orElseThrow(() -> new RuntimeException("Ubicacion not found with id: " + idUbicacion));
        ubicacion.setEstado(ubicacion.getEstado() == EstadoActivoInactivo.A ? EstadoActivoInactivo.I : EstadoActivoInactivo.A);
        ubicacionRepository.save(ubicacion);
    }
}
