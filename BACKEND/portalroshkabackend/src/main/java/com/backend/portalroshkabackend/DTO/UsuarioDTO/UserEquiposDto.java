package com.backend.portalroshkabackend.DTO.UsuarioDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEquiposDto {
    Integer idEquipo;
    String nombre;
    String lider;
    Integer porcentajeTrabajo;
}
