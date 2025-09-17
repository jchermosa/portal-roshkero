package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.self.*;
import com.backend.portalroshkabackend.Models.*;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;

import java.sql.Date;
import java.time.LocalDate;

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

    public static SolicitudTHResponseDto toSolicitudTHResponseDto(SolicitudesTH solicitudesTH){
        SolicitudTHResponseDto dto = new SolicitudTHResponseDto();

        dto.setIdSolicitudTH(solicitudesTH.getIdSolicitudTH());
        dto.setFechaInicio(solicitudesTH.getFechaInicio());
        dto.setFechaFin(solicitudesTH.getFechaFin());
        dto.setUsuario(solicitudesTH.getUsuario());
        dto.setCantidadDias(solicitudesTH.getCantidadDias());
        dto.setAprobacionTH(solicitudesTH.getAprobacionTH());
        dto.setComentario(solicitudesTH.getComentario());
        dto.setSolicitudThTipo(solicitudesTH.getSolicitudThTipo());
        dto.setEstado(solicitudesTH.getEstado());
        dto.setFechaCreacion(solicitudesTH.getFechaCreacion());
        dto.setPermisos(solicitudesTH.getPermisos());
        dto.setBeneficios(solicitudesTH.getBeneficios());

        return dto;

    }

    public static BenefitsTypesResponseDto toBenefitsResponseDto(Beneficios beneficios ){
        BenefitsTypesResponseDto dto = new BenefitsTypesResponseDto();

        dto.setIdBeneficio(beneficios.getIdBeneficio());
        dto.setNombre(beneficios.getNombre());
        dto.setDescripcion(beneficios.getDescripcion());
        dto.setInicioVigencia(beneficios.getInicioVigencia());
        dto.setFinVigencia(beneficios.getFinVigencia());
        dto.setFechaCreacion(beneficios.getFechaCreacion());

        return dto;

    }

    public static DevicesTypesResponseDto toDevicesTypesResponseDto(TipoDispositivo tipoDispositivo){
        DevicesTypesResponseDto dto = new DevicesTypesResponseDto();

        dto.setIdInventario(tipoDispositivo.getIdInventario());
        dto.setNombre(tipoDispositivo.getNombre());
        dto.setDetalle(tipoDispositivo.getDetalle());
        dto.setFechaCreacion(tipoDispositivo.getFechaCreacion());

        return dto;

    }

    public static SolicitudTHTipoResponseDto toSolicitudTHTipoResponseDto(SolicitudThTipo solicitudThTipo){
        SolicitudTHTipoResponseDto dto = new SolicitudTHTipoResponseDto();

        dto.setIdSolicitudTHTipo(solicitudThTipo.getIdSolicitudTHTipo());
        dto.setNombre(solicitudThTipo.getNombre());
        dto.setFechaCreacion(solicitudThTipo.getFechaCreacion());

        return dto;
    }

    public static PermissionsTypesResponseDto toPermissionsTypesResponseDto(Permisos permisos){
        PermissionsTypesResponseDto dto = new PermissionsTypesResponseDto();

        dto.setIdPermiso(permisos.getIdPermiso());
        dto.setNombre(permisos.getNombre());
        dto.setCantDias(permisos.getCantDias());

        return dto;
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

    public static void toUsuarioFromUpdateDto(Usuario user, UserUpdateDto updateDto){
        user.setNombre(updateDto.getNombre());
        user.setApellido(updateDto.getApellido());
        user.setNroCedula(updateDto.getNroCedula());
        user.setCorreo(updateDto.getCorreo());
        user.setRoles(updateDto.getRoles());
        user.setFechaIngreso(updateDto.getFechaIngreso());
        user.setEstado(updateDto.getEstado());
        user.setContrasena(updateDto.getContrasena());
        user.setTelefono(updateDto.getTelefono());
        user.setCargos(updateDto.getCargos());
        user.setFechaNacimiento(updateDto.getFechaNacimiento());
    }

    public static void toSolicitudesThFromSendDto(SolicitudesTH solicitudesTH, SendSolicitudDto dto){
        solicitudesTH.setFechaInicio(dto.getFechaInicio());
        solicitudesTH.setUsuario(dto.getUsuario());
        solicitudesTH.setCantidadDias(dto.getCantidadDias());
        solicitudesTH.setAprobacionTH(false);
        solicitudesTH.setComentario(dto.getComentario());
        solicitudesTH.setSolicitudThTipo(dto.getSolicitudThTipo());
        solicitudesTH.setEstado(EstadoSolicitudEnum.P);
        solicitudesTH.setFechaCreacion(Date.valueOf(LocalDate.now()));
        solicitudesTH.setPermisos(dto.getPermisos());
        solicitudesTH.setBeneficios(dto.getBeneficios());

    }

    public static void toSolicitudesThFromUpdateSolicitudDto(SolicitudesTH solicitudesTH, UpdateSolicitudDto dto){
        solicitudesTH.setSolicitudThTipo(dto.getSolicitudThTipo());
        solicitudesTH.setCantidadDias(dto.getCantidadDias());
        solicitudesTH.setFechaInicio(dto.getFechaInicio());
        solicitudesTH.setComentario(dto.getComentario());

    }

    public static MisSolicitudesResponseDto toMisSolicitudesResponseDto(SolicitudesTH solicitudesTH){
        MisSolicitudesResponseDto dto = new MisSolicitudesResponseDto();

        dto.setIdSolicitudTH(solicitudesTH.getIdSolicitudTH());
        dto.setIdSolicitudTH(solicitudesTH.getIdSolicitudTH());
        dto.setComentario(solicitudesTH.getComentario());
        dto.setAprobacionTH(solicitudesTH.getAprobacionTH());
        dto.setEstado(solicitudesTH.getEstado());
        dto.setFechaCreacion(solicitudesTH.getFechaCreacion());

        return dto;
    }

    public static SolicitudEspecificaResponseDto toSolicitudEspecificaResponseDto(SolicitudesTH solicitudesTH){
        SolicitudEspecificaResponseDto dto = new SolicitudEspecificaResponseDto();

        dto.setIdSolicitudTH(solicitudesTH.getIdSolicitudTH());
        dto.setSolicitudThTipo(solicitudesTH.getSolicitudThTipo());
        dto.setCantidadDias(solicitudesTH.getCantidadDias());
        dto.setFechaInicio(solicitudesTH.getFechaInicio());
        dto.setComentario(solicitudesTH.getComentario());

        return dto;

    }
}
