package com.backend.portalroshkabackend.DTO.th.cargos;

import com.backend.portalroshkabackend.Models.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CargoByIdResponseDto {
    private Integer idCargo;

    private String nombre;

    private List<UsuarioSimpleDto> empleadosAsignados;
}
