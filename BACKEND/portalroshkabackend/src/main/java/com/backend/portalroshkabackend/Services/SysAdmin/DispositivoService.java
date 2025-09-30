package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.DatabaseOperationException;
import com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos.*;
import com.backend.portalroshkabackend.tools.mapper.DispositivoMapper;
import com.backend.portalroshkabackend.tools.mapper.TipoDispositivoMapper;
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

import static com.backend.portalroshkabackend.tools.MessagesConst.DATABASE_DEFAULT_ERROR;


@Service
public class DispositivoService {


    @Autowired
    private DeviceTypesRepository deviceTypesRepository;

    @Autowired
    private UserService usuarioService;

    @Autowired
    private final  UbicacionService ubicacionService;

    @Autowired
    private final RepositoryService repositoryService;

    @Autowired
    private final DeviceRepository deviceRepository;

    private final Map<String, BiFunction<String, Pageable,Page<Dispositivo>>> sortingMapPage;


    public DispositivoService(DeviceTypesRepository deviceTypesRepository,
                             DeviceRepository deviceRepository,
                             UbicacionService ubicacionService,
                              RepositoryService repositoryService) {
        this.deviceTypesRepository = deviceTypesRepository;
        this.deviceRepository = deviceRepository;
        this.ubicacionService = ubicacionService;
        this.repositoryService = repositoryService;

        sortingMapPage = new HashMap<>();

        sortingMapPage.put("tipoDispositivo", (idStr, pageable) ->
            deviceRepository.findAllByTipoDispositivo_IdTipoDispositivoAndEncargadoIsNull(Integer.parseInt(idStr), pageable)
        );
    }


    // Listar los tipos de dispositivos

    @Transactional(readOnly = true)
    public DeviceDTO getDeviceById(Integer id) {
        Dispositivo dispositivo = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return DispositivoMapper.toDeviceDto(dispositivo);
    }

    @Transactional(readOnly = true)
    public Page<DeviceDTO> getAllDevices(Pageable pageable) {
        try {
            Page<Dispositivo> dispositivos = deviceRepository.findAll(pageable);
            return dispositivos.map(DispositivoMapper::toDeviceDto);
        } catch (Exception e) {
            System.err.println("Error al obtener dispositivos: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseOperationException("Error al acceder a los datos",e);
        }
    }

    // OBTENRE TODOS LOS TIPOS DE DISPOSITIVOS

    @Transactional
    public Page<DeviceTypeDTO> getAllTypes(Pageable pageable){
        Page<TipoDispositivo> tiposDispositivos = deviceTypesRepository.findAll(pageable);
        return tiposDispositivos.map(TipoDispositivoMapper::toDeviceTypeDto);
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
                dispositivos = deviceRepository.findAllWithoutOwner(pageable);
                throw new InvalidFilterValueException(filterValue);
            }
        }
        
        return dispositivos.map(DispositivoMapper::toDeviceDto);
    }
    

    // CRUD DEVICES

    @Transactional
    public DeviceDTO createDevice(DeviceDTO dispositivo) {

        Dispositivo newDispositivo = new Dispositivo();

        TipoDispositivo tipoDispositivo = repositoryService.findByIdOrThrow(
                deviceTypesRepository,
                dispositivo.getTipoDispositivo(),
                () -> new DeviceTypeNotFoundException(dispositivo.getTipoDispositivo())
        );
        // Asignar TipoDispositivo si se proporciona
        newDispositivo.setTipoDispositivo(tipoDispositivo);

        // Asignar la ubicacion si se proporciona
        UbicacionDto ubicacion = ubicacionService.findByIdUbicacion(dispositivo.getUbicacion())
                .orElseThrow(() -> new LocationNotFoundException(dispositivo.getUbicacion()));
        Ubicacion newUbicacion = new Ubicacion();
        DispositivoMapper.toUbicacionFromUbicacionDto(newUbicacion, ubicacion);

        // Asignar Encargado si se proporciona
        if (dispositivo.getEncargado() != null) {
            Optional<Usuario> encargado = usuarioService.getUsuario(dispositivo.getEncargado());
            newDispositivo.setEncargado(encargado.orElse(null));
        } else {
            newDispositivo.setEncargado(null);
        };

        // Asignar la nueva ubicacion al dispositivo
        newDispositivo.setUbicacion(newUbicacion);
        newDispositivo.setNroSerie(dispositivo.getNroSerie());
        newDispositivo.setModelo(dispositivo.getModelo());
        newDispositivo.setFechaFabricacion(dispositivo.getFechaFabricacion());
        newDispositivo.setCategoria(dispositivo.getCategoria());
        newDispositivo.setDetalles(dispositivo.getDetalle());
        newDispositivo.setEstado(dispositivo.getEstado());
        newDispositivo.setFechaCreacion(LocalDateTime.now());

        repositoryService.save(
                deviceRepository,
                newDispositivo,
                DATABASE_DEFAULT_ERROR
        );

        return DispositivoMapper.toDeviceDto(newDispositivo);

    }

    @Transactional
    public DeviceDTO updateDevice(Integer id, DeviceDTO dispositivoDto) {
       
        Dispositivo existingDispositivo = repositoryService.findByIdOrThrow(
                deviceRepository,
                id,
                () -> new DeviceNotFoundException(id)
        );

        existingDispositivo.setNroSerie(dispositivoDto.getNroSerie());


        // Asignar Tipo de Dispositivo si se proporciona
        if (dispositivoDto.getTipoDispositivo() == null)
            throw new RuntimeException("El tipo de dispositivo es obligatorio");

        TipoDispositivo tipoDispositivo = repositoryService.findByIdOrThrow(
                deviceTypesRepository,
                dispositivoDto.getTipoDispositivo(),
                () -> new DeviceTypeNotFoundException(dispositivoDto.getTipoDispositivo())
        );

        existingDispositivo.setTipoDispositivo(tipoDispositivo);

        UbicacionDto ubicacionDto = ubicacionService.findByIdUbicacion(dispositivoDto.getUbicacion())
                .orElseThrow(() -> new LocationNotFoundException(dispositivoDto.getUbicacion()));
        

        // Asignar la nueva ubicacion al dispositivo
        Ubicacion ubicacion = new Ubicacion();
        DispositivoMapper.toUbicacionFromUbicacionDto(ubicacion, ubicacionDto);


        //Pasar del DTO a la entidad
        DispositivoMapper.toDispositivoFromDto(existingDispositivo, dispositivoDto);

        Dispositivo updatedDispositivo = repositoryService.save(
                deviceRepository,
                existingDispositivo,
                DATABASE_DEFAULT_ERROR
        );


        return DispositivoMapper.toDeviceDto(updatedDispositivo);
    }

    @Transactional
    public void deleteDeviceById(Integer id) {

        // Encontrar el dispositivo por ID
        Dispositivo dispositivo = repositoryService.findByIdOrThrow(
                deviceRepository,
                id,
                () -> new DeviceNotFoundException(id)
        );

        dispositivo.setEstado(EstadoInventario.ND);

        repositoryService.save(
                deviceRepository,
                dispositivo,
                DATABASE_DEFAULT_ERROR
        );

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
        throw new DeviceMappingException("Error al procesar los datos",e);
    }
    
    return dto;
}
}
