package com.backend.portalroshkabackend.Services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.DispositivoDto;
import com.backend.portalroshkabackend.Models.tipoDispositivo;
import com.backend.portalroshkabackend.Repositories.DispositivoRepository;


@Service
public class DispositivoService {

    @Autowired
    DispositivoRepository dispositivoRepository;

    public DispositivoService(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    // insert tipo de inventario
    @Transactional
    public DispositivoDto createDevice(DispositivoDto dispositivo) {

        // creando un nuevo dispositivo
        tipoDispositivo newDispositivo = new tipoDispositivo();
        newDispositivo.setNombre(dispositivo.getNombre());
        newDispositivo.setDetalle(dispositivo.getDetalle());
        newDispositivo.setFechaCreacion(new Date());

        tipoDispositivo savedDispositivo = dispositivoRepository.save(newDispositivo);
        
        return mapToDispositivoDto(savedDispositivo);
    }

    @Transactional(readOnly = true)
    public List<DispositivoDto> getAllDevices() {
        List<tipoDispositivo> dispositivos = dispositivoRepository.findAll();
        return dispositivos.stream().map(this::mapToDispositivoDto).collect(Collectors.toList());
    }

    @Transactional
    public DispositivoDto updateDevice(Integer id, DispositivoDto dispositivoDto) {
        tipoDispositivo existingDispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo not found with id: " + id));

        existingDispositivo.setNombre(dispositivoDto.getNombre());
        existingDispositivo.setDetalle(dispositivoDto.getDetalle());

        tipoDispositivo updatedDispositivo = dispositivoRepository.save(existingDispositivo);
        
        return mapToDispositivoDto(updatedDispositivo);
    }

    @Transactional
    public void deleteDeviceById(Integer id) {
        dispositivoRepository.deleteById(id);
    }
    
    private DispositivoDto mapToDispositivoDto(tipoDispositivo dispositivo) {
        DispositivoDto dto = new DispositivoDto();
        dto.setIdTipoInventario(dispositivo.getIdInventario());
        dto.setNombre(dispositivo.getNombre());
        dto.setDetalle(dispositivo.getDetalle());
        dto.setFechaCreacion(dispositivo.getFechaCreacion());
        return dto;
    }
}
