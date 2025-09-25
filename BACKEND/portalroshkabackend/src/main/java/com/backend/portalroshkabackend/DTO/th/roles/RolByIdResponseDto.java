package com.backend.portalroshkabackend.DTO.th.roles;

import com.backend.portalroshkabackend.DTO.th.cargos.UsuarioSimpleDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RolByIdResponseDto {

    private Integer idRol;

    private String nombre;

    private List<UsuarioSimpleDto> empleadosAsignados;
}
