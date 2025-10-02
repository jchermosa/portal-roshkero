package com.backend.portalroshkabackend.Services.SysAdmin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos.DeviceTypeNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.TipoDispositivoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceTypesRepository;

import jakarta.transaction.Transactional;

import static com.backend.portalroshkabackend.tools.MessagesConst.DATABASE_DEFAULT_ERROR;

@Service
public class DeviceTypeService {
    
    @Autowired
    private final DeviceTypesRepository deviceTypesRepository;

    @Autowired
    private final RepositoryService repositoryService;

    DeviceTypeService(DeviceTypesRepository deviceTypesRepository, RepositoryService repositoryService) {
        this.deviceTypesRepository = deviceTypesRepository;
        this.repositoryService = repositoryService;
    }

    @Transactional
    // Add service methods here
    public Page<DeviceTypeDTO> getAllDeviceTypes(Pageable pageable) {
        // Implement the logic to fetch all device types from the repository
        Page<TipoDispositivo> deviceTypes = deviceTypesRepository.findAll(pageable);
        // Convert entities to DTOs
        return deviceTypes.map(TipoDispositivoMapper::toDeviceTypeDto);
    }

    @Transactional
    public DeviceTypeDTO getDeviceTypeById(Integer id) {
        TipoDispositivo deviceType = deviceTypesRepository.findById(id)
                .orElseThrow(() -> new DeviceTypeNotFoundException(id));
        return TipoDispositivoMapper.toDeviceTypeDto(deviceType);
    }   

    // Create a CRUD for DeviceTypes 

    @Transactional
    public DeviceTypeDTO createDeviceType(DeviceTypeDTO dto) {
        TipoDispositivo tipoDispositivo = convertToEntity(dto);
        TipoDispositivo savedTipoDispositivo = repositoryService.save(
                deviceTypesRepository,
                tipoDispositivo,
                DATABASE_DEFAULT_ERROR
        );
        return TipoDispositivoMapper.toDeviceTypeDto(savedTipoDispositivo);
    }

    @Transactional
    public DeviceTypeDTO updateDeviceType(DeviceTypeDTO dto) {
        TipoDispositivo tipoDispositivo = convertToEntity(dto);
        TipoDispositivo updatedTipoDispositivo = repositoryService.save(
                deviceTypesRepository,
                tipoDispositivo,
                DATABASE_DEFAULT_ERROR
        );
        return TipoDispositivoMapper.toDeviceTypeDto(updatedTipoDispositivo);
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

}
