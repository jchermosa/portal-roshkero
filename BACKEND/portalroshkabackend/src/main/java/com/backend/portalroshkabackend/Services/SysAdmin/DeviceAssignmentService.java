package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class DeviceAssignmentService {

    @Autowired
    private final DeviceAssignmentRepository deviceAssignment;

    @Autowired
    private final DispositivoService dispositivoService;

    @Autowired
    private final DeviceRepository deviceRepository;

    @Autowired
    private final UsuariosService usuarioService;

    @Autowired
    private final SolicitudRepository solicitudRepository;

    public DeviceAssignmentService(DeviceAssignmentRepository deviceAssignment, DispositivoService dispositivoService, DeviceRepository deviceRepository, UsuariosService usuarioService, SolicitudRepository solicitudRepository) {
        this.deviceAssignment = deviceAssignment;
        this.dispositivoService = dispositivoService;
        this.deviceRepository = deviceRepository;
        this.usuarioService = usuarioService;
        this.solicitudRepository = solicitudRepository;
    }



     @Transactional
    public List<DeviceAssignmentDTO> listarAsignaciones() {
        List<DispositivoAsignado> dispositivosAsignados = deviceAssignment.findAll();
        
        return dispositivosAsignados.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
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
                throw new RuntimeException("Solicitud no encontrada con ID: " + deviceAssignmentDTO.getIdSolicitud());
            }

            // verificamos que sea del tipo Dispositivo
            if (checkSolicitud.get().getTipoSolicitud() != SolicitudesEnum.DISPOSITIVO) {
                throw new RuntimeException("La solicitud no es del tipo Dispositivo.");
            }

            solicitud.setIdSolicitud(deviceAssignmentDTO.getIdSolicitud());
            dispositivoAsignado.setSolicitud(solicitud);
        }


        dispositivoAsignado.setFechaEntrega(deviceAssignmentDTO.getFechaEntrega());
        dispositivoAsignado.setFechaDevolucion(deviceAssignmentDTO.getFechaDevolucion());
        dispositivoAsignado.setEstado(deviceAssignmentDTO.getEstadoAsignacion());
        dispositivoAsignado.setObservaciones(deviceAssignmentDTO.getObservaciones());   
        
        deviceAssignment.save(dispositivoAsignado);

        // Cambiando el estado del dispositivo a No Disponible


        Dispositivo dispositivo = deviceRepository.findById(deviceAssignmentDTO.getIdDispositivo())
            .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado con ID: " + deviceAssignmentDTO.getIdDispositivo()));

        dispositivo.setEstado(EstadoInventario.A);

        // Encontrar la solicitud para obtener el ID del usuario
        Solicitud solicitud = solicitudRepository.findById(deviceAssignmentDTO.getIdSolicitud())
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + deviceAssignmentDTO.getIdSolicitud()));



        Optional<Usuario> encargadoOpt = usuarioService.getUsuario(solicitud.getUsuario().getIdUsuario());

        if (encargadoOpt.isPresent()) {
            dispositivo.setEncargado(encargadoOpt.get());
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + solicitud.getUsuario().getIdUsuario());
        }

        // Cambiar estado a No Disponible
        deviceRepository.save(dispositivo);

        deviceAssignmentDTO.setIdDispositivoAsignado(dispositivoAsignado.getIdDispositivoAsignado());
        return deviceAssignmentDTO;
    }



    @Transactional
    public DeviceAssignmentDTO actualizarAsignacion(Integer idDispositivoAsignado, DeviceAssignmentDTO deviceAssignmentDTO) {
        DispositivoAsignado dispositivoAsignado = deviceAssignment.findById(idDispositivoAsignado)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + idDispositivoAsignado));

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

        dispositivoAsignado.setFechaEntrega(deviceAssignmentDTO.getFechaEntrega());
        dispositivoAsignado.setFechaDevolucion(deviceAssignmentDTO.getFechaDevolucion());
        dispositivoAsignado.setEstado(deviceAssignmentDTO.getEstadoAsignacion());
        dispositivoAsignado.setObservaciones(deviceAssignmentDTO.getObservaciones());

        DispositivoAsignado savedAsignacion = deviceAssignment.save(dispositivoAsignado);

        return convertToDTO(savedAsignacion);
    }


    @Transactional
    public void eliminarAsignacion(Integer idDispositivoAsignado) {
        
        Optional<DispositivoAsignado> dispositivoAsignado = deviceAssignment.findById(idDispositivoAsignado);

        if (dispositivoAsignado.isPresent()) {
            DispositivoAsignado asignado = dispositivoAsignado.get();
            asignado.setEstado(EstadoAsignacion.D);
            deviceAssignment.save(asignado);
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



    private DeviceAssignmentDTO convertToDTO(DispositivoAsignado dispositivoAsignado) {
    DeviceAssignmentDTO dto = new DeviceAssignmentDTO();
    
    dto.setIdDispositivoAsignado(dispositivoAsignado.getIdDispositivoAsignado());
    dto.setFechaEntrega(dispositivoAsignado.getFechaEntrega());
    dto.setFechaDevolucion(dispositivoAsignado.getFechaDevolucion());
    dto.setEstadoAsignacion(dispositivoAsignado.getEstado());
    dto.setObservaciones(dispositivoAsignado.getObservaciones());
    
    // Mapear el dispositivo si existe
    if (dispositivoAsignado.getDispositivo() != null) {
        dto.setIdDispositivo(dispositivoAsignado.getDispositivo().getIdDispositivo());
    }
    
    // Mapear la solicitud si existe
    if (dispositivoAsignado.getSolicitud() != null) {
        dto.setIdSolicitud(dispositivoAsignado.getSolicitud().getIdSolicitud());
    }

    return dto;
}





}
