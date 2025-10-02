package com.backend.portalroshkabackend.DTO.Operationes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.backend.portalroshkabackend.Exception.ClienteExists;
import com.backend.portalroshkabackend.Exception.ValidEstado;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

// @UniqueNombre   //для проверки по уникальности имени для create, для проверки уникальности Для update
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquiposRequestDto {

    private Integer idEquipo;

    private Integer idLider;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaLimite;

    private Integer idCliente;

    private List<Integer> idTecnologias;

    @NotNull(message = "El estado es obligatorio")
    @ValidEstado
    private EstadoActivoInactivo estado;

    private List<UsuarioAsignacionDto> usuarios;

    private List<DiaUbicacionDto> equipoDiaUbicacion;
}
