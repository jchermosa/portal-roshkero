package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.th.cargos.*;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CargosMapper {

    // -------- DTO FROM ENTITY --------

    public static CargosResponseDto toCargosResponseDto(Cargos cargo){
        CargosResponseDto dto = new CargosResponseDto();

        dto.setIdCargo(cargo.getIdCargo());
        dto.setNombre(cargo.getNombre());
        dto.setFechaCreacion(cargo.getFechaCreacion());

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

    public static CargosDefaultResponseDto toCargosDefaultResponseDto(Integer idCargo, String message){
        CargosDefaultResponseDto dto = new CargosDefaultResponseDto();

        dto.setIdCargo(idCargo);
        dto.setMessage(message);

        return dto;
    }

    public static CargoByIdResponseDto toCargoByIdResponseDto(Cargos cargo, List<Usuario> empleados){
        CargoByIdResponseDto dto = new CargoByIdResponseDto();

        dto.setIdCargo(cargo.getIdCargo());
        dto.setNombre(cargo.getNombre());

        List<UsuarioSimpleDto> empleadosDto = empleados.stream()
                .map(CargosMapper::toUsuarioSimpleDto)
                .collect(Collectors.toList());

        dto.setEmpleadosAsignados(empleadosDto);

        return dto;
    }

    // -------- ENTITY FROM DTO --------

    public static void toCargosFromInsertDto(Cargos cargo, CargoInsertDto insertDto){

        cargo.setNombre(insertDto.getNombre());
        cargo.setFechaCreacion(LocalDate.now());

    }


}
