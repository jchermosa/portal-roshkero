package com.backend.portalroshkabackend.DTO;

import jakarta.validation.constraints.*;
import java.sql.Date;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    private Integer idRol;

    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private Date fechaIngreso;


    private EstadoActivoInactivo estado;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "Teléfono inválido")
    private String telefono;

    private Integer idEquipo;

    private Integer idCargo;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date fechaNacimiento;

    private boolean requiereCambioContrasena;


}
