package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.DispositivoDto;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Repositories.DeviceRepository;
import com.backend.portalroshkabackend.Repositories.DeviceTypesRepository;
import com.backend.portalroshkabackend.Repositories.TipoDispositivoRepository;


@Service
public class DispositivoService {

    @Autowired
    TipoDispositivoRepository tipoDispositivoRepository;

    @Autowired
    private DeviceTypesRepository deviceTypesRepository;

    @Autowired
    private final DeviceRepository deviceRepository;

    public DispositivoService(TipoDispositivoRepository tipoDispositivoRepository, DeviceTypesRepository deviceTypesRepository, DeviceRepository deviceRepository) {
        this.tipoDispositivoRepository = tipoDispositivoRepository;
        this.deviceTypesRepository = deviceTypesRepository;
        this.deviceRepository = deviceRepository;
    }


    // Listar los tipos de dispositivos

    @Transactional(readOnly = true)
    public List<DeviceDTO> getAllDevices() {
        try {
            List<Dispositivo> dispositivos = deviceRepository.findAll();
            return dispositivos.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error al obtener dispositivos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener dispositivos", e);
        }
    }

    // OBTENRE TODOS LOS TIPOS DE DISPOSITIVOS

    @Transactional
    public List<DeviceTypeDTO> getAllTypes(){
        List<TipoDispositivo> tiposDispositivos = deviceTypesRepository.findAll();
        return tiposDispositivos.stream()
                .map(this::convertToDto)
                .toList();
    }




    // CRUD DEVICES

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
        DispositivoDto dto = new DispositivoDto();
        dto.setIdTipoInventario(dispositivo.getIdTipoDispositivo());
        dto.setNombre(dispositivo.getNombre());
        dto.setDetalle(dispositivo.getDetalle());
        dto.setFechaCreacion(dispositivo.getFechaCreacion());
        return dto;
    }


    // CONVERTIR A LOS DTO
    private DeviceTypeDTO convertToDto(TipoDispositivo tipoDispositivo) {
        DeviceTypeDTO dto = new DeviceTypeDTO();
        dto.setIdTipoDispositivo(tipoDispositivo.getIdTipoDispositivo());
        dto.setNombre(tipoDispositivo.getNombre());
        dto.setDetalle(tipoDispositivo.getDetalle());
        return dto;
    }

    private DeviceDTO convertToDto(Dispositivo dispositivo) {
    DeviceDTO dto = new DeviceDTO();
    try {
        dto.setIdDispositivo(dispositivo.getIdDispositivo());
        dto.setNroSerie(dispositivo.getNroSerie());
        dto.setModelo(dispositivo.getModelo());
        dto.setFechaFabricacion(dispositivo.getFechaFabricacion());
        dto.setCategoria(dispositivo.getCategoria().name());
        dto.setDetalle(dispositivo.getDetalles());
        dto.setEstado(dispositivo.getEstado());
        dto.setEncargado(dispositivo.getEncargado().getIdUsuario());
        
        // Manejo seguro de relaciones lazy
        try {
            if (dispositivo.getTipoDispositivo() != null) {
                dto.setTipoDispositivo(dispositivo.getTipoDispositivo().getIdTipoDispositivo());
            }
        } catch (Exception lazyException) {
            System.err.println("Error cargando tipo dispositivo: " + lazyException.getMessage());
            dto.setTipoDispositivo(null);
        }
        
        try {
            if (dispositivo.getUbicacion() != null) {
                dto.setUbicacion(dispositivo.getUbicacion().getIdUbicacion());
            }
        } catch (Exception lazyException) {
            System.err.println("Error cargando ubicación: " + lazyException.getMessage());
            dto.setUbicacion(null);
        }
        
    } catch (Exception e) {
        System.err.println("Error general al convertir Dispositivo a DTO: " + e.getMessage());
        // Crear DTO mínimo con solo ID
        dto = new DeviceDTO();
        dto.setIdDispositivo(dispositivo.getIdDispositivo());
    }
    
    return dto;
}

}
