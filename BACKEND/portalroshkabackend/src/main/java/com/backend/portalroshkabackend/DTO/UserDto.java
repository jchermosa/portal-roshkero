package com.backend.portalroshkabackend.DTO;

import java.sql.Date;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Integer idUsuario;

    private String nombre;
    private String apellido;
    private int nroCedula;
    private String correo;
    private Roles roles;
    private Date fechaIngreso;
    private String antiguedad;
    private int diasVacaciones;
    private EstadoActivoInactivo estado;
    private String contrasena;
    private String telefono;
    private Cargos cargos;
    private Date fechaNacimiento;
    private int diasVacacionesRestante;
    private boolean requiereCambioContrasena;
    private int disponibilidad;




}
