package com.backend.portalroshkabackend.common.model;


import jakarta.persistence.*;
import org.postgresql.util.PGInterval;

import java.sql.Date;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "nro_cedula")
    private int nroCedula;

    @Column(name = "correo")
    private String correo;

    @Column(name = "id_rol")
    private int idRol;

    @Column(name = "fecha_ingreso")
    private java.sql.Date fechaIngreso;

    @Column(name = "antiguedad", insertable = false, updatable = false)
    private String antiguedad;

    @Column(name = "dias_vacaciones", insertable = false, updatable = false)
    private int diasVacaciones;

    @Column(name = "estado")
    private boolean estado;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "id_equipo")
    private int idEquipo;

    @Column(name = "id_cargo")
    private int idCargo;

    @Column(name = "fecha_nacimiento")
    private java.sql.Date fechaNacimiento;

    @Column(name = "dias_vacaciones_restante")
    private int diasVacacionesRestante;

    @Column(name = "requiere_cambio_contrasena")
    private boolean requiereCambioContrasena;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getNroCedula() {
        return nroCedula;
    }

    public void setNroCedula(int nroCedula) {
        this.nroCedula = nroCedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(String antiguedad) {
        this.antiguedad = antiguedad;
    }

    public int getDiasVacaciones() {
        return diasVacaciones;
    }

    public void setDiasVacaciones(int diasVacaciones) {
        this.diasVacaciones = diasVacaciones;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getDiasVacacionesRestante() {
        return diasVacacionesRestante;
    }

    public void setDiasVacacionesRestante(int diasVacacionesRestante) {
        this.diasVacacionesRestante = diasVacacionesRestante;
    }

    public boolean isRequiereCambioContrasena() {
        return requiereCambioContrasena;
    }

    public void setRequiereCambioContrasena(boolean requiereCambioContrasena) {
        this.requiereCambioContrasena = requiereCambioContrasena;
    }

}
