package com.backend.portalroshkabackend.DTO.common;

import java.time.LocalDate;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateDto {
    private String nombre;

    private String apellido;

    private String nroCedula;

    private String correo;

    private Roles roles;

    private LocalDate fechaIngreso;

    private EstadoActivoInactivo estado;

    private String telefono;

    private Cargos cargos;

    private LocalDate fechaNacimiento;

    private boolean requiereCambioContrasena;

    private Integer disponibilidad;

}
