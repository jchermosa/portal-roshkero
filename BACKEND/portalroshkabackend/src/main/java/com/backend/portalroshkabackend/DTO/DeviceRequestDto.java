package com.backend.portalroshkabackend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class DeviceRequestDto {
    @NotNull ( message = "La fecha de inicio es Obligatoria")
    @FutureOrPresent (message = "La fecha de inicio no puede ser en el pasado")
    private Date fechaInicio;

    @NotNull ( message = "Se debe indicar la cantidad de dias solicitados")
    private int cantDias;

    private boolean aprobacionAdmin;

    private String comentario;

    private EstadoSolicitudEnum estado;

    @NotNull ( message = "El tipo de dispositivo es Obligatorio")
    @Positive ( message = "El id debe ser positivo")
    private int idTipoDispositivo;

    @NotNull ( message = "El usuario que realiza la solicitud es Obligatorio")
    @Positive ( message = "El id debe ser positivo")
    private int idUsuario;



}
