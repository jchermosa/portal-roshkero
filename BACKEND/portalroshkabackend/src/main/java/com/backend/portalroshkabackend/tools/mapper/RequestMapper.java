package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.th.SolicitudResponseDto;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Solicitud;

public class RequestMapper {

    public static RequestResponseDto toRequestResponseDto(Integer idSolicitud, String message){
        RequestResponseDto dto = new RequestResponseDto();

        dto.setId(idSolicitud);
        dto.setMessage(message);

        return dto;
    }

    public static SolicitudResponseDto toSolicitudTHResponseDto(Solicitud solicitud){
        SolicitudResponseDto dto = new SolicitudResponseDto();

        dto.setIdSolicitud(solicitud.getIdSolicitud());
        dto.setFechaInicio(solicitud.getFechaInicio());
        dto.setUsuario(solicitud.getUsuario().getNombre() + " " + solicitud.getUsuario().getApellido());
        dto.setCantidadDias(solicitud.getCantDias());
        dto.setTipoSolicitud(solicitud.getTipoSolicitud().name());
        dto.setEstado(solicitud.getEstado());
        dto.setFechaCreacion(solicitud.getFechaCreacion());

        return dto;

    }
}
