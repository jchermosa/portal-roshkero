package com.backend.portalroshkabackend.DTO.UsuarioDTO;

import java.time.LocalDate;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.FocoEnum;
import com.backend.portalroshkabackend.Models.Enum.SeniorityEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String nroCedula;
    private String correo;
    private Integer idRol;
    private String nombreRol;
    private LocalDate fechaIngreso;
    private String antiguedad;
    private Integer diasVacaciones;
    private EstadoActivoInactivo estado;
    private String telefono;
    private Integer idCargo;
    private LocalDate fechaNacimiento;
    private Integer diasVacacionesRestante;
    private Boolean requiereCambioContrasena;
    private SeniorityEnum seniority;
    private FocoEnum foco;
    private String urlPerfil;
    private Integer disponibilidad;
}
