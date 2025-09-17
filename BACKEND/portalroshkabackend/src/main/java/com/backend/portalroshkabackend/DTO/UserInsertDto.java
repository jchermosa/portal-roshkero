package com.backend.portalroshkabackend.DTO;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Roles;
import jakarta.validation.constraints.*;
import java.sql.Date;
import java.time.LocalDate;

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

    private Roles roles;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private LocalDate fechaIngreso;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "Teléfono inválido")
    private String telefono;

    private Cargos Cargos;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date fechaNacimiento;

    private boolean requiereCambioContrasena;

    private EstadoActivoInactivo estado;

    private String url_perfil;

    private Integer disponibilidad;
}
