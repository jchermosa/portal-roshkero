package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioisRequestDto {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;

}
