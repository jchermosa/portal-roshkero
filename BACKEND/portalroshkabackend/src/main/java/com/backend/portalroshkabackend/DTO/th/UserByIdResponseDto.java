package com.backend.portalroshkabackend.DTO.th;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

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

    private java.sql.Date fechaNacimiento;

    private Roles roles;

    private Cargos cargos;

    private EstadoActivoInactivo estado;

}
