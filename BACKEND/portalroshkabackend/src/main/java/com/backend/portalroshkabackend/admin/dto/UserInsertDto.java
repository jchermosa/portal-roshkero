package com.backend.portalroshkabackend.admin.dto;

import java.sql.Date;

public class UserInsertDto {

    private String nombre;

    private String apellido;

    private int nro_cedula;

    private String correo;

    private int id_rol;

    private Date fecha_ingreso;

    private int dias_vacaciones;

    private boolean estado;

    private String contrasena;

    private String telefono;

    private int id_equipo;

    private int id_cargo;

    private Date fecha_nacimiento;

    private int dias_vacaciones_restante;

    private boolean requiere_cambio_contrasena;
}
