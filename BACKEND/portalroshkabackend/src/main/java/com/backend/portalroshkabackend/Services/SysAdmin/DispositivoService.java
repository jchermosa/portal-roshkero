package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.UbicacionDto;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Models.Ubicacion;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.EstadoInventario;
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
    private final  UbicacionService ubicacionService;


    @Autowired
    private final DeviceRepository deviceRepository;

    private final Map<String, Function<String, List<Dispositivo>>> sortingMap;


    public DispositivoService(DeviceTypesRepository deviceTypesRepository,
                             DeviceRepository deviceRepository,
                             UbicacionService ubicacionService) {
        this.deviceTypesRepository = deviceTypesRepository;
        this.deviceRepository = deviceRepository;
        this.ubicacionService = ubicacionService;


        sortingMap = new HashMap<>();

        sortingMap.put("tipoDispositivo", idStr -> 
            deviceRepository.findAllByTipoDispositivo_IdTipoDispositivo(Integer.parseInt(idStr))
        );
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


    // Listar los dispositivos que no tienen duenho 
    public List<DeviceDTO> getAllDevicesWithoutOwner(String sortBy, String filterValue) {

    List<Dispositivo> dispositivos;

    // Caso default: sin filtro
    if (sortBy == null || sortBy.isBlank() || !sortingMap.containsKey(sortBy)) {
        dispositivos = deviceRepository.findAllWithoutOwner();
    } else {
        // Aplico el filtro que está en el mapa (ej: tipoDispositivo)
        dispositivos = sortingMap.get(sortBy).apply(filterValue);

        // Filtrar solo los que no tienen dueño
        dispositivos = dispositivos.stream()
                .filter(d -> d.getEncargado() == null)
                .toList();
    }

    return dispositivos.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
}


    

    // CRUD DEVICES

    @Transactional
    public DeviceDTO createDevice(DeviceDTO dispositivo) {

        Dispositivo newDispositivo = new Dispositivo();


        // Asignar TipoDispositivo si se proporciona
        if (dispositivo.getTipoDispositivo() != null) {
            TipoDispositivo tipoDispositivo = deviceTypesRepository.findById(dispositivo.getTipoDispositivo())
                    .orElseThrow(() -> new RuntimeException("Tipo de dispositivo no encontrado con ID: " + dispositivo.getTipoDispositivo()));
            newDispositivo.setTipoDispositivo(tipoDispositivo);
        }
 
        // Asignar la ubicacion si se proporciona
        UbicacionDto ubicacion = ubicacionService.findByIdUbicacion(dispositivo.getUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + dispositivo.getUbicacion()));

        Ubicacion newUbicacion = new Ubicacion();
        newUbicacion.setIdUbicacion(ubicacion.getIdUbicacion());
        newUbicacion.setNombre(ubicacion.getNombre());
        newUbicacion.setEstado(ubicacion.getEstado());

        // Asignar la nueva ubicacion al dispositivo
        newDispositivo.setUbicacion(newUbicacion);



        newDispositivo.setNroSerie(dispositivo.getNroSerie());
        newDispositivo.setModelo(dispositivo.getModelo());
        newDispositivo.setFechaFabricacion(dispositivo.getFechaFabricacion());
        newDispositivo.setCategoria(dispositivo.getCategoria());
        newDispositivo.setDetalles(dispositivo.getDetalle());
        newDispositivo.setEstado(dispositivo.getEstado());

        // Asignar Encargado si se proporciona
        if (dispositivo.getEncargado() != null) {
            Optional<Usuario> encargado = usuarioService.getUsuario(dispositivo.getEncargado());
            newDispositivo.setEncargado(encargado.orElse(null));
        } else {
            newDispositivo.setEncargado(null);
        }
        
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

        // Asignar Tipo de Dispositivo si se proporciona
        if (dispositivoDto.getTipoDispositivo() != null) {
            TipoDispositivo tipoDispositivo = deviceTypesRepository.findById(dispositivoDto.getTipoDispositivo())
                    .orElseThrow(() -> new RuntimeException("Tipo de dispositivo no encontrado con ID: " + dispositivoDto.getTipoDispositivo()));
            existingDispositivo.setTipoDispositivo(tipoDispositivo);
        } else {
            throw new RuntimeException("El tipo de dispositivo es obligatorio");
        }


        
        UbicacionDto ubicacionDto = ubicacionService.findByIdUbicacion(dispositivoDto.getUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + dispositivoDto.getUbicacion()));
        

        // Asignar la nueva ubicacion al dispositivo
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setNombre(ubicacionDto.getNombre());
        ubicacion.setEstado(ubicacionDto.getEstado());
        ubicacion.setIdUbicacion(ubicacionDto.getIdUbicacion());

        existingDispositivo.setUbicacion(ubicacion); 

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
        

        // Encontrar el dispositivo por ID
        Dispositivo dispositivo = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado con ID: " + id));
        
        dispositivo.setEstado(EstadoInventario.ND);
        deviceRepository.save(dispositivo);
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
        
        // Verificar si el encargado existe antes de acceder a su ID
        if (dispositivo.getEncargado() != null) {
            dto.setEncargado(dispositivo.getEncargado().getIdUsuario());
        } else {
            dto.setEncargado(null); // o puedes omitir esta línea si el DTO ya inicializa como null
        }
        
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
        e.printStackTrace(); // Agregar esto para ver el stack trace completo
        // En lugar de crear un DTO vacío, propagar la excepción o manejar específicamente
        throw new RuntimeException("Error al convertir dispositivo a DTO", e);
    }
    
    return dto;
}
}
