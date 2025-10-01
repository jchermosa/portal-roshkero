package com.backend.portalroshkabackend.DTO.th;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

import org.springframework.cglib.core.Local;

@Data
@NoArgsConstructor
public class UserByIdResponseDto {
    private Integer idUsuario;

    private String nombre;

    private String apellido;

    private int nroCedula;

    private String correo;

    private String telefono;

    private LocalDate fechaIngreso;

    private LocalDate fechaNacimiento;

    private Roles roles;

    private Cargos cargos;

    private EstadoActivoInactivo estado;

}
