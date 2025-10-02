package com.backend.portalroshkabackend.DTO.th.cargos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioSimpleDto {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;

}
