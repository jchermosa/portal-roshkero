package com.backend.portalroshkabackend.Services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.DispositivoDto;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Repositories.TipoDispositivoRepository;


@Service
public class DispositivoService {

    @Autowired
    TipoDispositivoRepository tipoDispositivoRepository;

    public DispositivoService(TipoDispositivoRepository tipoDispositivoRepository) {
        this.tipoDispositivoRepository = tipoDispositivoRepository;
    }

    // insert tipo de inventario
    @Transactional
    public DispositivoDto createDevice(DispositivoDto dispositivo) {

        // creando un nuevo dispositivo
        TipoDispositivo newDispositivo = new TipoDispositivo();
        TipoDispositivo newDispositivo = new TipoDispositivo();
        newDispositivo.setNombre(dispositivo.getNombre());
        newDispositivo.setDetalle(dispositivo.getDetalle());
        newDispositivo.setFechaCreacion(new Date());

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
                .orElseThrow(() -> new RuntimeException("Dispositivo not found with id: " + id));

        existingDispositivo.setNombre(dispositivoDto.getNombre());
        existingDispositivo.setDetalle(dispositivoDto.getDetalle());

        TipoDispositivo updatedDispositivo = tipoDispositivoRepository.save(existingDispositivo);
        
        return mapToDispositivoDto(updatedDispositivo);
    }

    @Transactional
    public void deleteDeviceById(Integer id) {
        tipoDispositivoRepository.deleteById(id);
    }
    
    private DispositivoDto mapToDispositivoDto(TipoDispositivo dispositivo) {
    private DispositivoDto mapToDispositivoDto(TipoDispositivo dispositivo) {
        DispositivoDto dto = new DispositivoDto();
        dto.setIdTipoInventario(dispositivo.getIdTipoDispositivo());
        dto.setNombre(dispositivo.getNombre());
        dto.setDetalle(dispositivo.getDetalle());
        dto.setFechaCreacion(dispositivo.getFechaCreacion());
        return dto;
    }
}
