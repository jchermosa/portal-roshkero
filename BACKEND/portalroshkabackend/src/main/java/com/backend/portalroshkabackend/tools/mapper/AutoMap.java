package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.request.RequestDto;
import com.backend.portalroshkabackend.Models.*;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;

import java.time.LocalDateTime;

public class AutoMap {


    public static RequestDto toRequestDto(Solicitud request){
        RequestDto requestDto = new RequestDto();

        requestDto.setIdSolicitud(request.getIdSolicitud());
        requestDto.setFechaInicio(request.getFechaInicio());
        requestDto.setFechaFin(request.getFechaFin());
        requestDto.setEstado(request.getEstado());
        requestDto.setIdUsuario(request.getUsuario().getIdUsuario());
        requestDto.setCantidadDias(request.getCantDias());
        requestDto.setComentario(request.getComentario());

        return requestDto;
    }


}
