package com.backend.portalroshkabackend.DTO.Operationes;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UsuarioEquipoUpdateRequestDto {

    private LocalDate fechaEntrada; // можно менять
    private Double porcentajeTrabajo; // можно менять
    private Integer idTecnologia; // можно менять
    private Integer idEquipo; // можно менять

    public UsuarioEquipoUpdateRequestDto() {
    }
}