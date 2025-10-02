package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.asignacionDispositivos.DeviceAssignmentNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.asignacionDispositivos.InvalidRequestTypeException;
import com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos.DeviceNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudDispositivos.DeviceRequestNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.UserNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.DeviceAssignmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceAssignmentDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.DispositivoAsignado;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoAsignacion;
import com.backend.portalroshkabackend.Models.Enum.EstadoInventario;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceAssignmentRepository;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceRepository;
import com.backend.portalroshkabackend.Repositories.TH.SolicitudRepository;
import com.backend.portalroshkabackend.Services.UsuariosService;

import jakarta.transaction.Transactional;

import static com.backend.portalroshkabackend.tools.MessagesConst.DATABASE_DEFAULT_ERROR;

@Service
public class DeviceAssignmentService {

    @Autowired
    private final DeviceAssignmentRepository deviceAssignment;

    @Autowired
    private final DispositivoService dispositivoService;

    @Autowired
    private final RepositoryService repositoryService;
    @Autowired
    private final DeviceRepository deviceRepository;

    @Autowired
    private final UsuariosService usuarioService;

    @Autowired
    private final SolicitudRepository solicitudRepository;

    public DeviceAssignmentService(DeviceAssignmentRepository deviceAssignment, DispositivoService dispositivoService, RepositoryService repositoryService, DeviceRepository deviceRepository, UsuariosService usuarioService, SolicitudRepository solicitudRepository) {
        this.deviceAssignment = deviceAssignment;
        this.dispositivoService = dispositivoService;
        this.repositoryService = repositoryService;
        this.deviceRepository = deviceRepository;
        this.usuarioService = usuarioService;
        this.solicitudRepository = solicitudRepository;
    }



     @Transactional
    public Page<DeviceAssignmentDTO> listarAsignaciones(Pageable pageable) {
        Page<DispositivoAsignado> dispositivosAsignados = deviceAssignment.findAll(pageable);
        
        return dispositivosAsignados.map(DeviceAssignmentMapper::toDeviceAssignmentDto);
    }

    @Transactional
    public DeviceAssignmentDTO obtenerAsignacion(Integer idDispositivoAsignado) {
        DispositivoAsignado dispositivoAsignado = deviceAssignment.findById(idDispositivoAsignado)
            .orElseThrow(() -> new DeviceAssignmentNotFoundException(idDispositivoAsignado));

        return DeviceAssignmentMapper.toDeviceAssignmentDto(dispositivoAsignado);
    }

    @Transactional
    public DeviceAssignmentDTO crearAsignacion(DeviceAssignmentDTO deviceAssignmentDTO) {
       
        DispositivoAsignado dispositivoAsignado = new DispositivoAsignado();
        // dispositivoAsignado.setIdDispositivoAsignado(deviceAssignmentDTO.getIdDispositivoAsignado());

        // devolver el dispositivo si existe
        if (deviceAssignmentDTO.getIdDispositivo() != null) {
            Dispositivo dispositivo = new Dispositivo();
            dispositivo.setIdDispositivo(deviceAssignmentDTO.getIdDispositivo());
            dispositivoAsignado.setDispositivo(dispositivo);
        }


        // devolver la solicitud si es que existe 

        if (deviceAssignmentDTO.getIdSolicitud() != null) {
            Solicitud solicitud = new Solicitud();

            // Verificar el tipo de la solicitud
            Optional<Solicitud> checkSolicitud = solicitudRepository.findById(deviceAssignmentDTO.getIdSolicitud());

            // Verificamos que exista 
            if (checkSolicitud.isEmpty()) {
                throw new DeviceRequestNotFoundException(deviceAssignmentDTO.getIdSolicitud());
            }

            // verificamos que sea del tipo Dispositivo
            if (checkSolicitud.get().getTipoSolicitud() != SolicitudesEnum.DISPOSITIVO) {
                throw new InvalidRequestTypeException(checkSolicitud.get().getTipoSolicitud());
            }

            solicitud.setIdSolicitud(deviceAssignmentDTO.getIdSolicitud());
            dispositivoAsignado.setSolicitud(solicitud);
        }


        DeviceAssignmentMapper.toDispositivoAsignadoFromDto(dispositivoAsignado,deviceAssignmentDTO);
        deviceAssignment.save(dispositivoAsignado);

        // Cambiando el estado del dispositivo a No Disponible


        Dispositivo dispositivo = deviceRepository.findById(deviceAssignmentDTO.getIdDispositivo())
            .orElseThrow(() -> new DeviceNotFoundException(deviceAssignmentDTO.getIdDispositivo()));

        dispositivo.setEstado(EstadoInventario.A);

        // Encontrar la solicitud para obtener el ID del usuario
        Solicitud solicitud = solicitudRepository.findById(deviceAssignmentDTO.getIdSolicitud())
            .orElseThrow(() -> new DeviceRequestNotFoundException(deviceAssignmentDTO.getIdSolicitud()));



        Optional<Usuario> encargadoOpt = usuarioService.getUsuario(solicitud.getUsuario().getIdUsuario());

        if (encargadoOpt.isPresent()) {
            dispositivo.setEncargado(encargadoOpt.get());
        } else {
            throw new UserNotFoundException(solicitud.getUsuario().getIdUsuario());
        }

        // Cambiar estado a No Disponible
        repositoryService.save(
                deviceRepository,
                dispositivo,
                DATABASE_DEFAULT_ERROR
        );

        deviceAssignmentDTO.setIdDispositivoAsignado(dispositivoAsignado.getIdDispositivoAsignado());
        return deviceAssignmentDTO;
    }



    @Transactional
    public DeviceAssignmentDTO actualizarAsignacion(Integer idDispositivoAsignado, DeviceAssignmentDTO deviceAssignmentDTO) {
        DispositivoAsignado dispositivoAsignado = deviceAssignment.findById(idDispositivoAsignado)
            .orElseThrow(() -> new DeviceAssignmentNotFoundException(idDispositivoAsignado));

        // Mapear campos del DTO al modelo existente
        if (deviceAssignmentDTO.getIdDispositivo() != null) {
            Dispositivo dispositivo = new Dispositivo();
            dispositivo.setIdDispositivo(deviceAssignmentDTO.getIdDispositivo());
            dispositivoAsignado.setDispositivo(dispositivo);
        }

        if (deviceAssignmentDTO.getIdSolicitud() != null) {
            Solicitud solicitud = new Solicitud();
            solicitud.setIdSolicitud(deviceAssignmentDTO.getIdSolicitud());
            dispositivoAsignado.setSolicitud(solicitud);
        }

        DeviceAssignmentMapper.toDispositivoAsignadoFromDto(dispositivoAsignado,deviceAssignmentDTO);

        DispositivoAsignado savedAsignacion = repositoryService.save(
                deviceAssignment,
                dispositivoAsignado,
                DATABASE_DEFAULT_ERROR
        );

        return DeviceAssignmentMapper.toDeviceAssignmentDto(savedAsignacion);
    }


    @Transactional
    public void eliminarAsignacion(Integer idDispositivoAsignado) {
        
        Optional<DispositivoAsignado> dispositivoAsignado = deviceAssignment.findById(idDispositivoAsignado);

        if (dispositivoAsignado.isPresent()) {
            DispositivoAsignado asignado = dispositivoAsignado.get();
            asignado.setEstado(EstadoAsignacion.D);
            repositoryService.save(
                    deviceAssignment,
                    asignado,
                    DATABASE_DEFAULT_ERROR
            );
        }


        // cambia el estado del dispositivo a disponible
        Dispositivo dispositivo = dispositivoAsignado.get().getDispositivo();

        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setTipoDispositivo(dispositivo.getTipoDispositivo().getIdTipoDispositivo());
        deviceDTO.setUbicacion(dispositivo.getUbicacion().getIdUbicacion());
        deviceDTO.setNroSerie(dispositivo.getNroSerie());
        deviceDTO.setModelo(dispositivo.getModelo());
        deviceDTO.setDetalle(dispositivo.getDetalles());
        deviceDTO.setCategoria(dispositivo.getCategoria());
        deviceDTO.setFechaFabricacion(dispositivo.getFechaFabricacion());
        deviceDTO.setEncargado(null);
        deviceDTO.setEstado(EstadoInventario.D); // Disponible  
        dispositivoService.updateDevice(dispositivo.getIdDispositivo(),deviceDTO);

        
    }




}






