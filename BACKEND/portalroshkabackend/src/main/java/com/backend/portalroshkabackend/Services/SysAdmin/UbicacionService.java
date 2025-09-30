package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.List;
import java.util.Optional;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceRepository;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos.DtoMappingException;
import com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos.LocationNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.DispositivoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.SYSADMIN.UbicacionDto;
import com.backend.portalroshkabackend.Models.Ubicacion;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.UbicacionCRUDRepository;

import jakarta.transaction.Transactional;

import static com.backend.portalroshkabackend.tools.MessagesConst.DATABASE_DEFAULT_ERROR;

@Service
public class UbicacionService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired 
    private final UbicacionCRUDRepository ubicacionRepository;

    @Autowired
    private final RepositoryService repositoryService;

    public UbicacionService(UbicacionCRUDRepository ubicacionRepository, RepositoryService repositoryService) {
        this.ubicacionRepository = ubicacionRepository;
        this.repositoryService = repositoryService;
    }


    // Encontrar ubicacion por ID

    @Transactional
    public Optional<UbicacionDto> findByIdUbicacion(Integer idUbicacion) {
        return ubicacionRepository.findByIdUbicacion(idUbicacion)
                .map(ubicacion -> {
                    try {
                        // Obtener dispositivos de esta ubicación
                        List<Dispositivo> dispositivos = deviceRepository.findAllInLocation(idUbicacion);

                        UbicacionDto dto = new UbicacionDto();
                        dto.setIdUbicacion(ubicacion.getIdUbicacion());
                        dto.setNombre(ubicacion.getNombre());
                        dto.setEstado(ubicacion.getEstado());
                        dto.setDispositivos(dispositivos.stream()
                                .map(DispositivoMapper::toDeviceDto)
                                .toList());

                        return dto;
                    } catch (Exception e) {
                        throw new DtoMappingException("Error al procesar los datos", e);
                    }
                });
    }



    @Transactional
    // Encontrar todas las ubicaciones
    public Page<UbicacionDto> getAllUbicaciones(Pageable pageable){
        Page<Ubicacion> ubicaciones = ubicacionRepository.findAll(pageable);
        return ubicaciones.map(ubicacion -> {
        List<Dispositivo> dispositivos = deviceRepository.findAllInLocation(ubicacion.getIdUbicacion());
            try {
                UbicacionDto dto = new UbicacionDto();
                dto.setIdUbicacion(ubicacion.getIdUbicacion());
                dto.setNombre(ubicacion.getNombre());
                dto.setEstado(ubicacion.getEstado());
                dto.setDispositivos(dispositivos.stream()
                        .map(DispositivoMapper::toDeviceDto)
                        .toList());
                return dto;
            } catch (Exception e) {
                throw new DtoMappingException("Error al procesar los datos",e);
            }
                });
    }
    

    // crear nueva ubicacion
    @Transactional
    public UbicacionDto createUbicacion(UbicacionDto ubicacionDto){
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setNombre(ubicacionDto.getNombre());
        ubicacion.setEstado(ubicacionDto.getEstado());
        repositoryService.save(
                ubicacionRepository,
                ubicacion,
                DATABASE_DEFAULT_ERROR
        );
        ubicacionDto.setIdUbicacion(ubicacion.getIdUbicacion());
        return ubicacionDto;
    }


    // actualizar ubicacion
    @Transactional
    public Optional<UbicacionDto> updateUbicacion(Integer idUbicacion, UbicacionDto ubicacionDto){
        return ubicacionRepository.findByIdUbicacion(idUbicacion)
                .map(ubicacion -> {
                    try {
                        ubicacion.setNombre(ubicacionDto.getNombre());
                        ubicacion.setEstado(ubicacionDto.getEstado());
                        repositoryService.save(
                                ubicacionRepository,
                                ubicacion,
                                DATABASE_DEFAULT_ERROR
                        );
                        ubicacionDto.setIdUbicacion(ubicacion.getIdUbicacion());
                        return ubicacionDto;
                    }catch (Exception e){
                        throw new DtoMappingException("Error al procesar los datos",e);
                    }
                });
    }

    // eliminar ubicacion
    @Transactional
    public void deleteUbicacion(Integer idUbicacion){
        Ubicacion ubicacion = ubicacionRepository.findByIdUbicacion(idUbicacion)
            .orElseThrow(() -> new LocationNotFoundException(idUbicacion));
        ubicacion.setEstado(ubicacion.getEstado() == EstadoActivoInactivo.A ? EstadoActivoInactivo.I : EstadoActivoInactivo.A);
        repositoryService.save(
                ubicacionRepository,
                ubicacion,
                DATABASE_DEFAULT_ERROR
        );
    }
}
