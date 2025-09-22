package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;
import com.backend.portalroshkabackend.DTO.th.request.RequestDto;
import com.backend.portalroshkabackend.DTO.th.self.*;
import com.backend.portalroshkabackend.Models.*;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;

import java.time.LocalDateTime;

public class AutoMap {
    public static DefaultResponseDto toDefaultResponseDto(Integer idUsuario, String message){
        DefaultResponseDto dto = new DefaultResponseDto();

        dto.setIdUsuario(idUsuario);
        dto.setMessage(message);

        return dto;
    }

    public static UserResponseDto toUserResponseDto(Usuario user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setIdUsuario(user.getIdUsuario());
        dto.setNombreApellido(user.getNombre() + " " + user.getApellido());
        dto.setCorreo(user.getCorreo());
        dto.setAntiguedad(user.getAntiguedad());
        dto.setEstado(user.getEstado());

        return dto;
    }

    public static UserByIdResponseDto toUserByIdDto(Usuario user){
        UserByIdResponseDto dto = new UserByIdResponseDto();

        dto.setIdUsuario(user.getIdUsuario());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setNroCedula(user.getNroCedula());
        dto.setCorreo(user.getCorreo());
        dto.setTelefono(user.getTelefono());
        dto.setFechaIngreso(user.getFechaIngreso());
        dto.setFechaNacimiento(user.getFechaNacimiento());
        dto.setRoles(user.getRol());
        dto.setCargos(user.getCargo());
        dto.setEstado(user.getEstado());

        return dto;
    }

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

    public static RequestResponseDto toRequestResponseDto(Integer idSolicitud, String message){
        RequestResponseDto dto = new RequestResponseDto();

        dto.setId(idSolicitud);
        dto.setMessage(message);

        return dto;
    }

    public static SolicitudTHResponseDto toSolicitudTHResponseDto(Solicitud solicitudesTH){
        SolicitudTHResponseDto dto = new SolicitudTHResponseDto();

        dto.setIdSolicitudTH(solicitudesTH.getIdSolicitud());
        dto.setFechaInicio(solicitudesTH.getFechaInicio());
        dto.setUsuario(solicitudesTH.getUsuario().getNombre() + " " + solicitudesTH.getUsuario().getApellido());
        dto.setCantidadDias(solicitudesTH.getCantDias());
        dto.setSolicitudThTipo(solicitudesTH.getTipoSolicitud().name());
        dto.setEstado(solicitudesTH.getEstado());
        dto.setFechaCreacion(solicitudesTH.getFechaCreacion());

        return dto;

    }

    public static BenefitsTypesResponseDto toBenefitsResponseDto(TipoBeneficios beneficios ){
        BenefitsTypesResponseDto dto = new BenefitsTypesResponseDto();

        dto.setIdBeneficio(beneficios.getIdTipoBeneficio());
        dto.setNombre(beneficios.getNombre());
        dto.setDescripcion(beneficios.getDescripcion());
        dto.setVigencia(beneficios.getVigencia());
        return dto;

    }

    public static DevicesTypesResponseDto toDevicesTypesResponseDto(TipoDispositivo tipoDispositivo){
        DevicesTypesResponseDto dto = new DevicesTypesResponseDto();

        dto.setIdInventario(tipoDispositivo.getIdTipoDispositivo());
        dto.setNombre(tipoDispositivo.getNombre());
        dto.setDetalle(tipoDispositivo.getDetalle());

        return dto;

    }

    public static SolicitudTHTipoResponseDto toSolicitudTHTipoResponseDto(Solicitud solicitud){
        SolicitudTHTipoResponseDto dto = new SolicitudTHTipoResponseDto();

        dto.setIdSolicitudTHTipo(solicitud.getIdSolicitud());
        dto.setFechaCreacion(solicitud.getFechaCreacion());

        return dto;
    }

    public static PermissionsTypesResponseDto toPermissionsTypesResponseDto(TipoPermisos permisos){
        PermissionsTypesResponseDto dto = new PermissionsTypesResponseDto();

        dto.setIdPermiso(permisos.getIdTipoPermiso());
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
        user.setRol(insertDto.getRoles());
        user.setFechaIngreso(insertDto.getFechaIngreso());
        user.setEstado(insertDto.getEstado());
        user.setContrasena(insertDto.getContrasena());
        user.setTelefono(insertDto.getTelefono());
        user.setCargo(insertDto.getCargos());
        user.setFechaNacimiento(insertDto.getFechaNacimiento());
        user.setRequiereCambioContrasena(insertDto.isRequiereCambioContrasena());
        user.setUrlPerfil(insertDto.getUrl_perfil());
        user.setDiasVacaciones(insertDto.getDisponibilidad());
        user.setDisponibilidad(insertDto.getDisponibilidad());

        return user;
    }

    public static void toUsuarioFromUpdateDto(Usuario user, UserUpdateDto updateDto){
        user.setNombre(updateDto.getNombre());
        user.setApellido(updateDto.getApellido());
        user.setNroCedula(updateDto.getNroCedula());
        user.setCorreo(updateDto.getCorreo());
        user.setRol(updateDto.getRoles());
        user.setFechaIngreso(updateDto.getFechaIngreso());
        user.setEstado(updateDto.getEstado());
        user.setContrasena(updateDto.getContrasena());
        user.setTelefono(updateDto.getTelefono());
        user.setCargo(updateDto.getCargos());
        user.setFechaNacimiento(updateDto.getFechaNacimiento());
    }

    public static void toSolicitudesThFromSendDto(Solicitud solicitud, SendSolicitudDto dto){
        solicitud.setFechaInicio(dto.getFechaInicio());
        solicitud.setUsuario(dto.getUsuario());
        solicitud.setCantDias(dto.getCantidadDias());
        solicitud.setComentario(dto.getComentario());
        solicitud.setTipoSolicitud(dto.getSolicitudThTipo());
        solicitud.setEstado(EstadoSolicitudEnum.P);
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setPermisoAsignado(dto.getPermisoAsignado());
        solicitud.setBeneficioAsignado(dto.getBeneficioAsignado());

    }

    public static void toSolicitudesThFromUpdateSolicitudDto(Solicitud solicitud, UpdateSolicitudDto dto){
        solicitud.setTipoSolicitud(dto.getSolicitudThTipo());
        solicitud.setCantDias(dto.getCantidadDias());
        solicitud.setFechaInicio(dto.getFechaInicio());
        solicitud.setComentario(dto.getComentario());

    }

    public static MisSolicitudesResponseDto toMisSolicitudesResponseDto(Solicitud solicitud){
        MisSolicitudesResponseDto dto = new MisSolicitudesResponseDto();
        dto.setIdSolicitudTH(solicitud.getIdSolicitud());
        dto.setComentario(solicitud.getComentario());
        dto.setEstado(solicitud.getEstado());
        dto.setFechaCreacion(solicitud.getFechaCreacion());

        return dto;
    }

    public static SolicitudEspecificaResponseDto toSolicitudEspecificaResponseDto(Solicitud solicitud){
        SolicitudEspecificaResponseDto dto = new SolicitudEspecificaResponseDto();

        dto.setIdSolicitudTH(solicitud.getIdSolicitud());
        dto.setSolicitudThTipo(solicitud.getTipoSolicitud());
        dto.setCantidadDias(solicitud.getCantDias());
        dto.setFechaInicio(solicitud.getFechaInicio());
        dto.setComentario(solicitud.getComentario());

        return dto;

    }
}
