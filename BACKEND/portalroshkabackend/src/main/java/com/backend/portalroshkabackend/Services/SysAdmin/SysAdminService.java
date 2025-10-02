package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.backend.portalroshkabackend.tools.errors.errorslist.DatabaseOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.SYSADMIN.RequestDTO;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.SysAdminRepository;

@Service
public class SysAdminService {

    @Autowired
    private SysAdminRepository sysAdminRepository;


    SysAdminService(SysAdminRepository sysAdminRepository) {
        this.sysAdminRepository = sysAdminRepository;
    }

    // Cambiar todos los métodos para retornar RequestDTO
    @Transactional(readOnly = true)
    public Page<RequestDTO> getAllRejectedRequest(Pageable pageable) {
        try {
            Page<Solicitud> solicitudesRechazadas = sysAdminRepository.findSolicitudesRechazadas(pageable);
            return solicitudesRechazadas.map(this::toDto);
        } catch (Exception e) {
            System.err.println("Error al obtener solicitudes rechazadas: " + e.getMessage());
            throw new DatabaseOperationException("Error al obtener las solicitudes rechazadas");
        }
    }

    @Transactional(readOnly = true)
    public Page<RequestDTO> getAllApprovedRequest(Pageable pageable) {
        try {
            Page<Solicitud> solicitudesAprobadas = sysAdminRepository.findSolicitudesAprobadas(pageable);
            return solicitudesAprobadas.map(this::toDto);
        } catch (Exception e) {
            System.err.println("Error al obtener solicitudes aprobadas: " + e.getMessage());
            throw new DatabaseOperationException("Error al obtener las solicitudes aprobadas");
        }
    }

    @Transactional(readOnly = true)
    public Page<RequestDTO> getAllPendingRequest(Pageable pageable) {
        try {
            Page<Solicitud> solicitudesPendientes = sysAdminRepository.findSolicitudesPendientes(pageable);
            return solicitudesPendientes.map(this::toDto);
        } catch (Exception e) {
            System.err.println("Error al obtener solicitudes pendientes: " + e.getMessage());
            throw new DatabaseOperationException("Error al obtener las solicitudes pendientes");
        }
    }

    @Transactional(readOnly = true)
    public Page<RequestDTO> findAllSolicitudes(Pageable pageable) {
        try {
            Page<Solicitud> solicitudes = sysAdminRepository.findAllSolicitudes(pageable);
            return solicitudes.map(this::toDto);
        } catch (Exception e) {
            System.err.println("Error al obtener las solicitudes: " + e.getMessage());
            throw new DatabaseOperationException("Error al obtener las solicitudes");
        }    }


    private RequestDTO toDto(Solicitud solicitud) {
        RequestDTO dto = new RequestDTO();
        try {
            dto.setIdSolicitud(solicitud.getIdSolicitud());
            
            // Manejo seguro de objetos que pueden ser null
            if (solicitud.getUsuario() != null) {
                dto.setIdUsuario(solicitud.getUsuario().getIdUsuario());
                dto.setNombreUsuario(solicitud.getUsuario().getNombre());
            }
            
            // if (solicitud.getDocumentoAdjunto() != null) {
            //     dto.setIdDocumentoAdjunto(solicitud.getDocumentoAdjunto().getIdDocumentoAdjunto());
            // }
            
            // if (solicitud.getLider() != null) {
            //     dto.setIdLider(solicitud.getLider().getIdUsuario());
            // }
            
            if (solicitud.getTipoSolicitud() != null) {
                dto.setTipoSolicitud(solicitud.getTipoSolicitud().name());
            }

            // Obtener el comentario y remover lo que tenga entre parentesis 
            String comentario = solicitud.getComentario();
            if (comentario != null) {
                comentario = comentario.replaceAll("\\(.*?\\)", "").trim();
            }
            dto.setComentario(comentario);

            if (solicitud.getEstado() != null) {
                dto.setEstado(solicitud.getEstado());
            }
            
            dto.setFechaInicio(solicitud.getFechaInicio());
            // dto.setCantDias(solicitud.getCantDias());
            dto.setFechaFin(solicitud.getFechaFin());
            
        } catch (Exception e) {
            System.err.println("Error al convertir Solicitud a DTO: " + e.getMessage());
            // En caso de error, al menos establecer el ID
            if (solicitud != null) {
                dto.setIdSolicitud(solicitud.getIdSolicitud());
            }
        }
        
        return dto;
    }
}