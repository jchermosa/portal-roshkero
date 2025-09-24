package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.SYSADMIN.RequestDTO;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Repositories.SysAdminRepository;

@Service
public class SysAdminService {

    @Autowired
    private SysAdminRepository sysAdminRepository;

    @Autowired
    private final DispositivoService dispositivoService;

    SysAdminService(SysAdminRepository sysAdminRepository, DispositivoService dispositivoService) {
        this.sysAdminRepository = sysAdminRepository;
        this.dispositivoService = dispositivoService;
    }

    // Cambiar todos los métodos para retornar RequestDTO
    @Transactional(readOnly = true)
    public List<RequestDTO> getAllRejectedRequest(){
        try {
            return convertToDto(sysAdminRepository.findSolicitudesRechazadas());
        } catch (Exception e) {
            System.err.println("Error al obtener solicitudes rechazadas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<RequestDTO> getAllApprovedRequest(){
        try {
            return convertToDto(sysAdminRepository.findSolicitudesAprobadas());
        } catch (Exception e) {
            System.err.println("Error al obtener solicitudes aprobadas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<RequestDTO> getAllPendingRequest(){
        try {
            return convertToDto(sysAdminRepository.findSolicitudesPendientes());
        } catch (Exception e) {
            System.err.println("Error al obtener solicitudes pendientes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<RequestDTO> findAllSolicitudes(){
        try {
            List<Solicitud> solicitudes = sysAdminRepository.findAllSolicitudes();
            return convertToDto(solicitudes);
        } catch (Exception e) {
            System.err.println("Error al obtener solicitudes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Transactional
    public void deleteDispositivo(Integer id){
        dispositivoService.deleteDeviceById(id);
    }

    private List<RequestDTO> convertToDto(List<Solicitud> solicitudes) {
        return solicitudes.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private RequestDTO toDto(Solicitud solicitud) {
        RequestDTO dto = new RequestDTO();
        try {
            dto.setIdSolicitud(solicitud.getIdSolicitud());
            
            // Manejo seguro de objetos que pueden ser null
            if (solicitud.getUsuario() != null) {
                dto.setIdUsuario(solicitud.getUsuario().getIdUsuario());
            }
            
            if (solicitud.getDocumentoAdjunto() != null) {
                dto.setIdDocumentoAdjunto(solicitud.getDocumentoAdjunto().getIdDocumentoAdjunto());
            }
            
            if (solicitud.getLider() != null) {
                dto.setIdLider(solicitud.getLider().getIdUsuario());
            }
            
            if (solicitud.getTipoSolicitud() != null) {
                dto.setTipoSolicitud(solicitud.getTipoSolicitud().name());
            }
            
            dto.setComentario(solicitud.getComentario());
            
            if (solicitud.getEstado() != null) {
                dto.setEstado(solicitud.getEstado().name());
            }
            
            dto.setFechaInicio(solicitud.getFechaInicio());
            dto.setCantDias(solicitud.getCantDias());
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