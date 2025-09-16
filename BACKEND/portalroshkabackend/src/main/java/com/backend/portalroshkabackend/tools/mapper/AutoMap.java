package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.*;
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
        dto.setRoles(user.getRoles());
        dto.setFechaIngreso(user.getFechaIngreso());
        dto.setAntiguedad(user.getAntiguedad());
        dto.setDiasVacaciones(user.getDiasVacaciones());
        dto.setEstado(user.getEstado());
        dto.setContrasena(user.getContrasena());
        dto.setTelefono(user.getTelefono());
        dto.setCargos(user.getCargos());
        dto.setFechaNacimiento(user.getFechaNacimiento());
        dto.setDiasVacacionesRestante(user.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(user.isRequiereCambioContrasena());
        dto.setDisponibilidad(user.getDisponibilidad());
        return dto;
    }

    public static RequestDto toRequestDto(Solicitudes request){
        RequestDto requestDto = new RequestDto();

        requestDto.setIdSolicitud(request.getIdSolicitud());
        requestDto.setFechaInicio(request.getFechaInicio());
        requestDto.setFechaFin(request.getFechaFin());
        requestDto.setEstado(request.getEstado());
        requestDto.setIdUsuario(request.getIdUsuario());
        requestDto.setCantidadDias(request.getCantidadDias());
        requestDto.setNumeroAprobaciones(request.getNumeroAprobaciones());
        requestDto.setComentario(request.getComentario());

        return requestDto;
    }

    public static PositionDto toPositionDto(Cargos position){
        PositionDto positionDto = new PositionDto();

        positionDto.setIdCargo(position.getIdCargo());
        positionDto.setNombre(position.getNombre());

        return positionDto;
    }

    public static EmailUpdatedDto toEmailUpdatedDto(Usuario user){
        EmailUpdatedDto emailUpdatedDto = new EmailUpdatedDto();

        emailUpdatedDto.setIdUsuario(user.getIdUsuario());
        emailUpdatedDto.setCorreo(user.getCorreo());

        return emailUpdatedDto;
    }

    public static PhoneUpdatedDto toPhoneUpdatedDto(Usuario user){
        PhoneUpdatedDto phoneUpdatedDto = new PhoneUpdatedDto();

        phoneUpdatedDto.setIdUsuario(user.getIdUsuario());
        phoneUpdatedDto.setTelefono(user.getTelefono());

        return phoneUpdatedDto;
    }

    public static Usuario toUsuarioFromInsertDto(UserInsertDto insertDto){
        Usuario user = new Usuario();
        user.setNombre(insertDto.getNombre());
        user.setApellido(insertDto.getApellido());
        user.setNroCedula(insertDto.getNroCedula());
        user.setCorreo(insertDto.getCorreo());
        user.setRoles(insertDto.getRoles());
        user.setFechaIngreso(insertDto.getFechaIngreso());
        user.setEstado(insertDto.getEstado());
        user.setContrasena(insertDto.getContrasena());
        user.setTelefono(insertDto.getTelefono());
        user.setCargos(insertDto.getCargos());
        user.setFechaNacimiento(insertDto.getFechaNacimiento());
        user.setRequiereCambioContrasena(insertDto.isRequiereCambioContrasena());
        user.setUrl(insertDto.getUrl_perfil());
        user.setDiasVacaciones(insertDto.getDisponibilidad());
        user.setDisponibilidad(insertDto.getDisponibilidad());

        return user;
    }
}
