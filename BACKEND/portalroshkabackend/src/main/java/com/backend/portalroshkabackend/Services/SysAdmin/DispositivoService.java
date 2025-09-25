package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceRepository;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceTypesRepository;
import com.backend.portalroshkabackend.Services.UsuariosService;
import com.backend.portalroshkabackend.Services.Operations.Service.UsuarioServiceImpl;
import com.backend.portalroshkabackend.Services.UsuarioServicio.UserService;


@Service
public class DispositivoService {


    @Autowired
    private DeviceTypesRepository deviceTypesRepository;

    @Autowired
    private UserService usuarioService;

    

    @Autowired
    private final DeviceRepository deviceRepository;

    public DispositivoService(DeviceTypesRepository deviceTypesRepository, DeviceRepository deviceRepository) {
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

    @Transactional
    public DeviceDTO createDevice(DeviceDTO dispositivo) {

        Dispositivo newDispositivo = new Dispositivo();
        newDispositivo.setNroSerie(dispositivo.getNroSerie());
        newDispositivo.setModelo(dispositivo.getModelo());
        newDispositivo.setFechaFabricacion(dispositivo.getFechaFabricacion());
        newDispositivo.setCategoria(dispositivo.getCategoria());
        newDispositivo.setDetalles(dispositivo.getDetalle());
        newDispositivo.setEstado(dispositivo.getEstado());
        
        Optional<Usuario>encargado = usuarioService.getUsuario(dispositivo.getEncargado());

        newDispositivo.setEncargado(encargado.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dispositivo.getEncargado())));
        
         // Establecer la fecha de creación al momento de crear el dispositivo
        newDispositivo.setFechaCreacion(LocalDateTime.now());

        // Asignar TipoDispositivo si se proporciona
        if (dispositivo.getTipoDispositivo() != null) {
            TipoDispositivo tipoDispositivo = deviceTypesRepository.findById(dispositivo.getTipoDispositivo())
                    .orElseThrow(() -> new RuntimeException("Tipo de dispositivo no encontrado con ID: " + dispositivo.getTipoDispositivo()));
            newDispositivo.setTipoDispositivo(tipoDispositivo);
        } else {
            newDispositivo.setTipoDispositivo(null);
        }

        // Aquí puedes asignar otros campos como Ubicacion y Encargado si es necesario
        deviceRepository.save(newDispositivo);

        return convertToDto(newDispositivo);

    }

    @Transactional
    public DeviceDTO updateDevice(Integer id, DeviceDTO dispositivoDto) {
       
        Dispositivo existingDispositivo = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado con ID: " + id));

        existingDispositivo.setNroSerie(dispositivoDto.getNroSerie());
        existingDispositivo.setModelo(dispositivoDto.getModelo());
        existingDispositivo.setFechaFabricacion(dispositivoDto.getFechaFabricacion());
        existingDispositivo.setCategoria(dispositivoDto.getCategoria());
        existingDispositivo.setDetalles(dispositivoDto.getDetalle());
        existingDispositivo.setEstado(dispositivoDto.getEstado());
        
        // Actualizar TipoDispositivo si se proporciona
        if (dispositivoDto.getTipoDispositivo() != null) {
            TipoDispositivo tipoDispositivo = deviceTypesRepository.findById(dispositivoDto.getTipoDispositivo())
                    .orElseThrow(() -> new RuntimeException("Tipo de dispositivo no encontrado con ID: " + dispositivoDto.getTipoDispositivo()));
            existingDispositivo.setTipoDispositivo(tipoDispositivo);
        } else {
            existingDispositivo.setTipoDispositivo(null);
        }

        // Aquí puedes actualizar otros campos como Ubicacion y Encargado si es necesario

        Dispositivo updatedDispositivo = deviceRepository.save(existingDispositivo);
        return convertToDto(updatedDispositivo);
    }

    @Transactional
    public void deleteDeviceById(Integer id) {
        deviceRepository.deleteById(id);
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
        dto.setNroSerie(dispositivo.getNroSerie());
        dto.setModelo(dispositivo.getModelo());
        dto.setFechaFabricacion(dispositivo.getFechaFabricacion());
        dto.setCategoria(dispositivo.getCategoria());
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
    }
    
    return dto;
}

}
