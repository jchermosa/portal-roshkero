package com.backend.portalroshkabackend.DTO.Operationes;

import java.time.LocalDate;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAsignacionDto {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private Integer porcentajeTrabajo;
    private LocalDate fechaEntrada;
    private LocalDate fechaFin;
    private EstadoActivoInactivo estado;
}
