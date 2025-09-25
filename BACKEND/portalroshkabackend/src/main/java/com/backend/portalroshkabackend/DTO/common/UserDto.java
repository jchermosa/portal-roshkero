package com.backend.portalroshkabackend.DTO.common;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserDto {

    private Integer idUsuario;

    private String nombre;

    private String apellido;

    private String nroCedula;

    private String correo;

    private Roles roles;

    private LocalDate fechaIngreso;

    private String antiguedad;

    private int diasVacaciones;

    private EstadoActivoInactivo estado;

    private String contrasena;

    private String telefono;

    private Cargos cargos;

    private LocalDate fechaNacimiento;

    private int diasVacacionesRestante;

    private boolean requiereCambioContrasena;

    private String url;

    private Integer disponibilidad;
}
