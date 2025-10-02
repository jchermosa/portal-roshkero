package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;
import com.backend.portalroshkabackend.Models.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class EmployeeMapper {

    // ------ ENTITY TO DTO ------

    public static DefaultResponseDto toDefaultResponseDto(Integer idUsuario, String message){
        DefaultResponseDto dto = new DefaultResponseDto();

        dto.setIdUsuario(idUsuario);
        dto.setMessage(message);

        return dto;
    }

    // Devolver todos los campos
    public static UserResponseDto toUserResponseDto(Usuario user) {

        UserResponseDto dto = new UserResponseDto();
        dto.setIdUsuario(user.getIdUsuario());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setNroCedula(user.getNroCedula());
        dto.setIdRol(user.getRol().getIdRol());
        dto.setRolNombre(user.getRol().getNombre());
        dto.setIdCargo(user.getCargo().getIdCargo());
        dto.setCargoNombre(user.getCargo().getNombre());
        dto.setFechaIngreso(user.getFechaIngreso());
        dto.setTelefono(user.getTelefono());
        dto.setFechaNacimiento(user.getFechaNacimiento());
        dto.setDiasVacaciones(user.getDiasVacaciones());
        dto.setDiasVacacionesRestante(user.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(user.getRequiereCambioContrasena());
        dto.setUrl(user.getUrlPerfil());
        dto.setFoco(user.getFoco());
        dto.setSeniority(user.getSeniority());
        dto.setDisponibilidad(user.getDisponibilidad());
        dto.setAntiguedad(user.getAntiguedad());
        dto.setEstado(user.getEstado());
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
        dto.setIdRol(user.getRol().getIdRol());
        dto.setRolNombre(user.getRol().getNombre());
        dto.setIdCargo(user.getCargo().getIdCargo());
        dto.setCargoNombre(user.getCargo().getNombre());
        dto.setFoco(user.getFoco());
        dto.setSeniority(user.getSeniority());
        dto.setDiasVacaciones(user.getDiasVacaciones());
        dto.setDiasVacacionesRestante(user.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(user.getRequiereCambioContrasena());
        dto.setUrl(user.getUrlPerfil());
        dto.setDisponibilidad(user.getDisponibilidad());
        dto.setAntiguedad(user.getAntiguedad());
        dto.setCargoNombre(user.getCargo().getNombre());
        dto.setEstado(user.getEstado());

        return dto;
    }

    public static UserDto toUserDto(Usuario user){
        UserDto dto = new UserDto();

        dto.setIdUsuario(user.getIdUsuario());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setNroCedula(user.getNroCedula());
        dto.setCorreo(user.getCorreo());
        // dto.setIdRol(user.getRol().getIdRol());
        // dto.setNombreRol(user.getRol().getNombre());
        dto.setFechaIngreso(user.getFechaIngreso());
        dto.setAntiguedad(user.getAntiguedad());
        dto.setDiasVacaciones(user.getDiasVacaciones());
        dto.setEstado(user.getEstado());
        dto.setTelefono(user.getTelefono());
        // dto.setIdCargo(user.getCargo().getIdCargo());
        dto.setFechaNacimiento(user.getFechaNacimiento());
        dto.setDiasVacacionesRestante(user.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(user.getRequiereCambioContrasena());
        dto.setSeniority(user.getSeniority());
        dto.setFoco(user.getFoco());
        dto.setUrlPerfil(user.getUrlPerfil());
        dto.setDisponibilidad(user.getDisponibilidad());

        return dto;
    }

    // ------ DTO TO ENTITY ------


    public static Usuario toUsuarioFromInsertDto(UserInsertDto insertDto){
        Usuario user = new Usuario();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setNombre(insertDto.getNombre());
        user.setApellido(insertDto.getApellido());
        user.setNroCedula(insertDto.getNroCedula());
        user.setCorreo(insertDto.getCorreo());
        user.setRol(insertDto.getRol());
        user.setFechaIngreso(insertDto.getFechaIngreso());
        user.setEstado(insertDto.getEstado());
        user.setContrasena(encoder.encode(insertDto.getNroCedula()));
        user.setTelefono(insertDto.getTelefono());
        user.setCargo(insertDto.getCargo());
        user.setFoco(insertDto.getFoco());
        user.setSeniority(insertDto.getSeniority());
        user.setFechaNacimiento(insertDto.getFechaNacimiento());

        Period period = Period.between(insertDto.getFechaIngreso(), LocalDate.now());

        if (period.getYears() >= 1 && period.getYears() <= 5){
            user.setDiasVacaciones(12);
        } else if (period.getYears() >= 5 && period.getYears() <= 10 ){
            user.setDiasVacaciones(15);
        } else if (period.getYears() >= 10 ){
            user.setDiasVacaciones(21);
        }

        user.setDiasVacacionesRestante(user.getDiasVacaciones());
        user.setRequiereCambioContrasena(insertDto.isRequiereCambioContrasena());
        user.setFechaCreacion(LocalDateTime.now());
        user.setUrlPerfil(insertDto.getUrl_perfil());
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
        user.setFoco(updateDto.getFoco());
        user.setSeniority(updateDto.getSeniority());
        user.setFechaNacimiento(updateDto.getFechaNacimiento());
        user.setDisponibilidad(updateDto.getDisponibilidad());
    }

}
