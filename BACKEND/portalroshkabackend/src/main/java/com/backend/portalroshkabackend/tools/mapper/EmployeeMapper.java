package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;
import com.backend.portalroshkabackend.Models.Usuario;

import java.time.LocalDateTime;

// TODO Endopoint reestablecer contrasena (hashear la contrasena al guardar, requiereCambioContrasena en true)


public class EmployeeMapper {
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
        dto.setRolId(user.getRol().getIdRol());
        dto.setRolNombre(user.getRol().getNombre());
        dto.setCargoId(user.getCargo().getIdCargo());
        dto.setCargoNombre(user.getCargo().getNombre());
        dto.setEstado(user.getEstado());

        return dto;
    }


    // ------ DTO TO ENTITY ------


    public static Usuario toUsuarioFromInsertDto(UserInsertDto insertDto){
        Usuario user = new Usuario();
        user.setNombre(insertDto.getNombre());
        user.setApellido(insertDto.getApellido());
        user.setNroCedula(insertDto.getNroCedula());
        user.setCorreo(insertDto.getCorreo());
        user.setRol(insertDto.getRol());
        user.setFechaIngreso(insertDto.getFechaIngreso());
        user.setEstado(insertDto.getEstado());
        user.setContrasena(insertDto.getContrasena());
        user.setTelefono(insertDto.getTelefono());
        user.setCargo(insertDto.getCargo());
        user.setFechaNacimiento(insertDto.getFechaNacimiento());
        user.setRequiereCambioContrasena(insertDto.isRequiereCambioContrasena());
        user.setFechaCreacion(LocalDateTime.now());
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
        user.setTelefono(updateDto.getTelefono());
        user.setCargo(updateDto.getCargos());
        user.setFechaNacimiento(updateDto.getFechaNacimiento());
    }

}
