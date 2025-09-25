package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.th.cargos.*;
import com.backend.portalroshkabackend.DTO.th.roles.RolByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolInsertDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolesResponseDto;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Roles;
import com.backend.portalroshkabackend.Models.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RolesMapper {
    // -------- DTO FROM ENTITY --------

    public static RolesResponseDto toRolesResponseDto(Roles roles){
        RolesResponseDto dto = new RolesResponseDto();

        dto.setIdCargo(roles.getIdRol());
        dto.setNombre(roles.getNombre());
        dto.setFechaCreacion(roles.getFechaCreacion());

        return dto;
    }

    private static UsuarioSimpleDto toUsuarioSimpleDto(Usuario usuario) {
        UsuarioSimpleDto dto = new UsuarioSimpleDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        return dto;
    }

    public static RolDefaultResponseDto toRolesDefaultResponseDto(Integer idRol, String message){
        RolDefaultResponseDto dto = new RolDefaultResponseDto();

        dto.setIdRol(idRol);
        dto.setMessage(message);

        return dto;
    }

    public static RolByIdResponseDto toRolByIdResponseDto(Roles rol, List<Usuario> empleados){
        RolByIdResponseDto dto = new RolByIdResponseDto();

        dto.setIdRol(rol.getIdRol());
        dto.setNombre(rol.getNombre());

        List<UsuarioSimpleDto> empleadosDto = empleados.stream()
                .map(RolesMapper::toUsuarioSimpleDto)
                .collect(Collectors.toList());

        dto.setEmpleadosAsignados(empleadosDto);

        return dto;
    }

    // -------- ENTITY FROM DTO --------

    public static void toRolesFromInsertDto(Roles rol, RolInsertDto insertDto){

        rol.setNombre(insertDto.getNombre());
        rol.setFechaCreacion(LocalDateTime.now());

    }
}
