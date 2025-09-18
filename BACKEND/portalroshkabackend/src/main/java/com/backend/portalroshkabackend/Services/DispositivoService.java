package com.backend.portalroshkabackend.Services;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.DispositivoDto;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Repositories.TipoDispositivoRepository;
import com.backend.portalroshkabackend.Exceptions.ResourceNotFoundException;


@Service
public class DispositivoService {

    private final TipoDispositivoRepository tipoDispositivoRepository;

    @Autowired
    public DispositivoService(TipoDispositivoRepository tipoDispositivoRepository) {
        this.tipoDispositivoRepository = tipoDispositivoRepository;
    }

    // insert tipo de inventario
    @Transactional
    public DispositivoDto createDevice(DispositivoDto dispositivo) {

        // creando un nuevo dispositivo
        TipoDispositivo newDispositivo = new TipoDispositivo();
        newDispositivo.setNombre(dispositivo.getNombre());
        newDispositivo.setDetalle(dispositivo.getDetalle());
        newDispositivo.setFechaCreacion(LocalDateTime.now());

        TipoDispositivo savedDispositivo = tipoDispositivoRepository.save(newDispositivo);
        
        return mapToDispositivoDto(savedDispositivo);
    }

    @Transactional(readOnly = true)
    public List<DispositivoDto> getAllDevices() {
        List<TipoDispositivo> dispositivos = tipoDispositivoRepository.findAll();
        return dispositivos.stream().map(this::mapToDispositivoDto).collect(Collectors.toList());
    }

    @Transactional
    public DispositivoDto updateDevice(Integer id, DispositivoDto dispositivoDto) {
        TipoDispositivo existingDispositivo = tipoDispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo not found with id: " + id));

        existingDispositivo.setNombre(dispositivoDto.getNombre());
        existingDispositivo.setDetalle(dispositivoDto.getDetalle());

        TipoDispositivo updatedDispositivo = tipoDispositivoRepository.save(existingDispositivo);
        
        return mapToDispositivoDto(updatedDispositivo);
    }

    @Transactional(readOnly = true)
    public DispositivoDto getDeviceById(Integer id) {
        TipoDispositivo dispositivo = tipoDispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo not found with id: " + id));
        return mapToDispositivoDto(dispositivo);
    }

    @Transactional
    public void deleteDeviceById(Integer id) {
        if (!tipoDispositivoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dispositivo not found with id: " + id);
        }
        tipoDispositivoRepository.deleteById(id);
    }
    
    private DispositivoDto mapToDispositivoDto(TipoDispositivo dispositivo) {
        DispositivoDto dto = new DispositivoDto();
        dto.setIdTipoInventario(dispositivo.getIdInventario());
        dto.setNombre(dispositivo.getNombre());
        dto.setDetalle(dispositivo.getDetalle());
        // Convert LocalDateTime to Date
        java.util.Date fechaCreacion = dispositivo.getFechaCreacion() != null ? 
                java.util.Date.from(dispositivo.getFechaCreacion().atZone(java.time.ZoneId.systemDefault()).toInstant()) : null;
        dto.setFechaCreacion(fechaCreacion);
        return dto;
    }
}
