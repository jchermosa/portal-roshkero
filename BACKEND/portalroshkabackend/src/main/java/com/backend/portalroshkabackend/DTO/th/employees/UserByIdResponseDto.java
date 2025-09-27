package com.backend.portalroshkabackend.DTO.th.employees;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.FocoEnum;
import com.backend.portalroshkabackend.Models.Enum.SeniorityEnum;
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
    private String nroCedula;
    private String correo;
    private Integer idRol;
    private String rolNombre;
    private Integer idCargo;
    private String cargoNombre;
    private LocalDate fechaIngreso;
    private String telefono;
    private FocoEnum foco;
    private SeniorityEnum seniority;
    private LocalDate fechaNacimiento;
    private EstadoActivoInactivo estado;
    private Integer disponibilidad;
    private Integer diasVacaciones;
    private Integer diasVacacionesRestante;
    private boolean requiereCambioContrasena;
    private String url;
    private String antiguedad;

}
