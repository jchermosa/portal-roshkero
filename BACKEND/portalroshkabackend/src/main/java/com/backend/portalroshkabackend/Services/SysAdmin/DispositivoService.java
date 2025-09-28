package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.UbicacionDto;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Models.Ubicacion;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoInventario;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceRepository;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceTypesRepository;
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

    private final Map<String, BiFunction<String, Pageable,Page<Dispositivo>>> sortingMapPage;


    public DispositivoService(DeviceTypesRepository deviceTypesRepository,
                             DeviceRepository deviceRepository,
                             UbicacionService ubicacionService) {
        this.deviceTypesRepository = deviceTypesRepository;
        this.deviceRepository = deviceRepository;
        this.ubicacionService = ubicacionService;


        sortingMapPage = new HashMap<>();

        sortingMapPage.put("tipoDispositivo", (idStr, pageable) ->
            deviceRepository.findAllByTipoDispositivo_IdTipoDispositivoAndEncargadoIsNull(Integer.parseInt(idStr), pageable)
        );
    }


    // Listar los tipos de dispositivos

    @Transactional(readOnly = true)
    public Page<DeviceDTO> getAllDevices(Pageable pageable) {
        try {
            Page<Dispositivo> dispositivos = deviceRepository.findAll(pageable);
            return dispositivos.map(this::convertToDto);
        } catch (Exception e) {
            System.err.println("Error al obtener dispositivos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener dispositivos", e);
        }
    }

    // OBTENRE TODOS LOS TIPOS DE DISPOSITIVOS

    @Transactional
    public Page<DeviceTypeDTO> getAllTypes(Pageable pageable){
        Page<TipoDispositivo> tiposDispositivos = deviceTypesRepository.findAll(pageable);
        return tiposDispositivos.map(this::convertToDto);
    }


    // Listar los dispositivos que no tienen duenho 
    // En DispositivoService.java
    public Page<DeviceDTO> getAllDevicesWithoutOwner(Pageable pageable, String sortBy, String filterValue) {
        Page<Dispositivo> dispositivos;

        // Considerar "default" como ausencia de filtro
        boolean shouldApplyFilter = sortBy != null && 
                                !sortBy.isBlank() && 
                                !"default".equals(sortBy) && 
                                sortingMapPage.containsKey(sortBy) &&
                                filterValue != null && 
                                !filterValue.isBlank();

        if (!shouldApplyFilter) {
            // Sin filtro: devolver todos los dispositivos sin dueño
            dispositivos = deviceRepository.findAllWithoutOwner(pageable);
        } else {
            try {
                // Con filtro: aplicar el filtro específico
                if ("tipoDispositivo".equals(sortBy)) {
                    Integer.parseInt(filterValue); // Validar que sea número
                }
                
                dispositivos = sortingMapPage.get(sortBy).apply(filterValue, pageable);
                
            } catch (NumberFormatException e) {
                System.err.println("Error: filterValue no es un número válido: " + filterValue);
                dispositivos = deviceRepository.findAllWithoutOwner(pageable);
            }
        }
        
        return dispositivos.map(this::convertToDto);
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
        dto.setIdDispositivo(dispositivo.getIdDispositivo());
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
