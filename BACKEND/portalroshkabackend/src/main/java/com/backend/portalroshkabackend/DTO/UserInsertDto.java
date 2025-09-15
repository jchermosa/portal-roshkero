package com.backend.portalroshkabackend.DTO;

import java.sql.Date;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInsertDto {

    private String nombre;

    private String apellido;

    private int nroCedula;

    private String correo;

    private Integer idRol;

    private Date fechaIngreso;


    private EstadoActivoInactivo estado;

    private String contrasena;

    private String telefono;

    private Integer idEquipo;

    private Integer idCargo;

    private Date fechaNacimiento;

    private boolean requiereCambioContrasena;


}
