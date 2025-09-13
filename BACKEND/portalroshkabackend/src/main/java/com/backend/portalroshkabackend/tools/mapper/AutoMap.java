package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.PositionDto;
import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.UserDto;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Solicitudes;
import com.backend.portalroshkabackend.Models.Usuario;

public class AutoMap {
    public static UserDto toUserDto(Usuario user) {
        UserDto dto = new UserDto();
        dto.setIdUsuario(user.getIdUsuario());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setNroCedula(user.getNroCedula());
        dto.setCorreo(user.getCorreo());
        dto.setIdRol(user.getIdRol());
        dto.setFechaIngreso(user.getFechaIngreso());
        dto.setAntiguedad(user.getAntiguedad());


        dto.setDiasVacaciones(user.getDiasVacaciones());
        dto.setEstado(user.isEstado());
        dto.setContrasena(user.getContrasena());
        dto.setTelefono(user.getTelefono());
        dto.setIdEquipo(user.getIdEquipo());
        dto.setIdCargo(user.getIdCargo());
        dto.setFechaNacimiento(user.getFechaNacimiento());
        dto.setDiasVacacionesRestante(user.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(user.isRequiereCambioContrasena());
        return dto;
    }

    public static RequestDto toRequestDto(Solicitudes request){
        RequestDto requestDto = new RequestDto();

        requestDto.setIdSolicitud(request.getIdSolicitud());
        requestDto.setFechaInicio(request.getFechaInicio());
        requestDto.setFechaFin(request.getFechaFin());
        requestDto.setEstado(request.isEstado());
        requestDto.setIdUsuario(request.getIdUsuario());
        requestDto.setCantidadDias(request.getCantidadDias());
        requestDto.setNumeroAprobaciones(request.getNumeroAprobaciones());
        requestDto.setComentario(request.getComentario());
        requestDto.setRechazado(request.isRechazado());

        return requestDto;
    }

    public static PositionDto toPositionDto(Cargos position){
        PositionDto positionDto = new PositionDto();

        positionDto.setIdCargo(position.getIdCargo());
        positionDto.setNombre(position.getNombre());

        return positionDto;
    }
}
