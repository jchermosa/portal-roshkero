package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioisResponseDto {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private Integer disponibilidad;

}
