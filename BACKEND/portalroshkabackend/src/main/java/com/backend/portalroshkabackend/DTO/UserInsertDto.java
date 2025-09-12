package com.backend.portalroshkabackend.DTO;

import jakarta.validation.constraints.*;
import java.sql.Date;

public class UserInsertDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Positive(message = "El número de cédula debe ser positivo")
    private int nroCedula;

    @Email(message = "Correo inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @Positive(message = "Debe especificar un rol válido")
    private int idRol;

    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private Date fechaIngreso;

    private boolean estado;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "Teléfono inválido")
    private String telefono;

    @PositiveOrZero(message = "El id del equipo debe ser 0 o positivo")
    private int idEquipo;

    @PositiveOrZero(message = "El id del cargo debe ser 0 o positivo")
    private int idCargo;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date fechaNacimiento;

    private boolean requiereCambioContrasena;

    // --- Getters y Setters ---

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public int getNroCedula() { return nroCedula; }
    public void setNroCedula(int nroCedula) { this.nroCedula = nroCedula; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getIdEquipo() { return idEquipo; }
    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }

    public int getIdCargo() { return idCargo; }
    public void setIdCargo(int idCargo) { this.idCargo = idCargo; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public boolean isRequiere_cambio_contrasena() { return requiereCambioContrasena; }
    public void setRequiereCambioContrasena(boolean requiereCambioContrasena) { this.requiereCambioContrasena = requiereCambioContrasena; }
}
