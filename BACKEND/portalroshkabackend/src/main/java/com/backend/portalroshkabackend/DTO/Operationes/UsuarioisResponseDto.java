package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.Data;

import java.sql.Date;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Roles;

@Data
public class UsuarioisResponseDto {
    
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private int nroCedula;
    private String correo;
    private Roles idRol;
    private Date fechaIngreso;
    private String antiguedad;
    private int diasVacaciones;
    private EstadoActivoInactivo estado;
    private String contrasena;
    private String telefono;
    private Cargos idCargo;
    private Date fechaNacimiento;
    private int diasVacacionesRestante;
    private boolean requiereCambioContrasena;
    private String url;
    private Integer disponibilidad;

    public UsuarioisResponseDto() {
    }
}
