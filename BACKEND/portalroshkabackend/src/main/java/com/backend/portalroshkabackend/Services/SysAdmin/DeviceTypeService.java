package com.backend.portalroshkabackend.Services.SysAdmin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceTypesRepository;

import jakarta.transaction.Transactional;

@Service
public class DeviceTypeService {
    
    @Autowired
    private final DeviceTypesRepository deviceTypesRepository;

    DeviceTypeService(DeviceTypesRepository deviceTypesRepository) {
        this.deviceTypesRepository = deviceTypesRepository;
    }

    @Transactional
    // Add service methods here
    public List<DeviceTypeDTO> getAllDeviceTypes() {
        // Implement the logic to fetch all device types from the repository
        List<TipoDispositivo> deviceTypes = deviceTypesRepository.findAll();
        // Convert entities to DTOs
        return deviceTypes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeviceTypeDTO getDeviceTypeById(Integer id) {
        TipoDispositivo deviceType = deviceTypesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device type not found with id: " + id));
        return convertToDTO(deviceType);
    }   

    // Create a CRUD for DeviceTypes 

    @Transactional
    public DeviceTypeDTO createDeviceType(DeviceTypeDTO dto) {
        TipoDispositivo tipoDispositivo = convertToEntity(dto);
        TipoDispositivo savedTipoDispositivo = deviceTypesRepository.save(tipoDispositivo);
        return convertToDTO(savedTipoDispositivo);
    }

    @Transactional
    public DeviceTypeDTO updateDeviceType(DeviceTypeDTO dto) {
        TipoDispositivo tipoDispositivo = convertToEntity(dto);
        TipoDispositivo updatedTipoDispositivo = deviceTypesRepository.save(tipoDispositivo);
        return convertToDTO(updatedTipoDispositivo);
    }

    @Transactional
    public void deleteDeviceType(Integer id) {
        deviceTypesRepository.deleteById(id);
    }

    public TipoDispositivo convertToEntity(DeviceTypeDTO dto) {
        TipoDispositivo tipoDispositivo = new TipoDispositivo();
        tipoDispositivo.setIdTipoDispositivo(dto.getIdTipoDispositivo());
        tipoDispositivo.setNombre(dto.getNombre());
        tipoDispositivo.setDetalle(dto.getDetalle());
        tipoDispositivo.setFechaCreacion(LocalDateTime.now());
        return tipoDispositivo;
    }

    private DeviceTypeDTO convertToDTO(TipoDispositivo tipoDispositivo) {
        DeviceTypeDTO dto = new DeviceTypeDTO();
        dto.setIdTipoDispositivo(tipoDispositivo.getIdTipoDispositivo());
        dto.setNombre(tipoDispositivo.getNombre());
        dto.setDetalle(tipoDispositivo.getDetalle());
        return dto;
    }
}
