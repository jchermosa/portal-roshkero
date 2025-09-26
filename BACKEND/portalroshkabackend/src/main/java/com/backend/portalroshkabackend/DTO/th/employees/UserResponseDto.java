package com.backend.portalroshkabackend.DTO.th.employees;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {
    private Integer idUsuario;

    private String nombreApellido;

    private String correo;

    private String antiguedad;
    private EstadoActivoInactivo estado;

}
