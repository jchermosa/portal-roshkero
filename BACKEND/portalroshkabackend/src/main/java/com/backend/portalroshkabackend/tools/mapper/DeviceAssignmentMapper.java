package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceAssignmentDTO;
import com.backend.portalroshkabackend.Models.DispositivoAsignado;

public class DeviceAssignmentMapper {

    // ===== ENTIDAD A DTO =====
    public static DeviceAssignmentDTO toDeviceAssignmentDto(DispositivoAsignado dispositivoAsignado) {
        if (dispositivoAsignado == null) return null;

        DeviceAssignmentDTO dto = new DeviceAssignmentDTO();
        dto.setIdDispositivoAsignado(dispositivoAsignado.getIdDispositivoAsignado());
        dto.setFechaEntrega(dispositivoAsignado.getFechaEntrega());
        dto.setFechaDevolucion(dispositivoAsignado.getFechaDevolucion());
        dto.setEstadoAsignacion(dispositivoAsignado.getEstado());
        dto.setObservaciones(dispositivoAsignado.getObservaciones());

        if (dispositivoAsignado.getDispositivo() != null) {
            dto.setIdDispositivo(dispositivoAsignado.getDispositivo().getIdDispositivo());
        }

        if (dispositivoAsignado.getSolicitud() != null) {
            dto.setIdSolicitud(dispositivoAsignado.getSolicitud().getIdSolicitud());
        }

        return dto;
    }

    // ===== DTO A ENTIDAD =====
    public static void toDispositivoAsignadoFromDto(DispositivoAsignado dispositivoAsignado, DeviceAssignmentDTO dto) {
        if (dto == null) return;

        dispositivoAsignado.setFechaEntrega(dto.getFechaEntrega());
        dispositivoAsignado.setFechaDevolucion(dto.getFechaDevolucion());
        dispositivoAsignado.setEstado(dto.getEstadoAsignacion());
        dispositivoAsignado.setObservaciones(dto.getObservaciones());

        // Las relaciones (Dispositivo, Solicitud) se manejan por separado
    }


    // DTO simplificado para listados
    public static DeviceAssignmentDTO toSimpleDto(DispositivoAsignado dispositivoAsignado) {
        if (dispositivoAsignado == null) return null;

        DeviceAssignmentDTO dto = new DeviceAssignmentDTO();
        dto.setIdDispositivoAsignado(dispositivoAsignado.getIdDispositivoAsignado());
        dto.setFechaEntrega(dispositivoAsignado.getFechaEntrega());
        dto.setFechaDevolucion(dispositivoAsignado.getFechaDevolucion());
        dto.setEstadoAsignacion(dispositivoAsignado.getEstado());

        return dto;
    }


}