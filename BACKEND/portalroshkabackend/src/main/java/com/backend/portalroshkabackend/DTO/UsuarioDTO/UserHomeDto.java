package com.backend.portalroshkabackend.DTO.UsuarioDTO;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Roles;

import java.sql.Date;
import java.util.List;

public class UserHomeDto {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private int nroCedula;
    private String contrasena;
    private String telefono;
    private Date fechaIngreso;
    private Date fechaNacimiento;
    private EstadoActivoInactivo estado;
    private boolean requiereCambioContrasena;
    // private String RolString;
    private Roles Rol;
    private Cargos Cargo;
    // private Equipos Equipo;
    private String antiguedad;
    private int diasVacaciones;
    private int diasVacacionesRestante;
    private List<Equipos> equipos;
    private List<String> Equipos;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
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

    public EstadoActivoInactivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoActivoInactivo estado) {
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

    public Roles getRol() {
        return Rol;
    }

    public void setRol(Roles rol) {
        Rol = rol;
    }
    
    public Cargos getCargo() {
        return Cargo;
    }
    public void setCargo(Cargos cargo) {
        Cargo = cargo;
    }
    // public Equipos getEquipo() {
    //     return Equipo;
    // }
    // public void setEquipo(Equipos equipo) {
    //     Equipo = equipo;
    // }

    // public List<Equipos> getEquipos() {
    //     return equipos;
    // }

    public void setEquipos(List<Equipos> equipos) {
        this.equipos = equipos;
        for (Equipos equipo : equipos) {
            if (this.Equipos == null) {
                this.Equipos = new java.util.ArrayList<>();
            }
            this.Equipos.add(equipo.getNombre());
        }
    }

    public List<String> getEquipos() {
        return Equipos;
    }

}
