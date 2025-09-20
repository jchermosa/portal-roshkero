package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UsuarioisResponseDto {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;

    public UsuarioisResponseDto(Integer idUsuario, String nombre, String apellido, String correo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
    }
}
