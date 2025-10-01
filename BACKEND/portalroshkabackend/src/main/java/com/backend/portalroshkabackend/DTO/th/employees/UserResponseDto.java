package com.backend.portalroshkabackend.DTO.th.employees;

import java.time.LocalDate;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.FocoEnum;
import com.backend.portalroshkabackend.Models.Enum.SeniorityEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {


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
    private String antiguedad;
    private Integer diasVacaciones;
    private String telefono;
    private LocalDate fechaNacimiento;
    private Integer diasVacacionesRestante;
    private boolean requiereCambioContrasena;
    private String url;
    private FocoEnum foco;
    private SeniorityEnum seniority;
    private Integer disponibilidad;
    private EstadoActivoInactivo estado;


}
