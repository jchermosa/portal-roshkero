package com.backend.portalroshkabackend.DTO.UsuarioDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class tiposPermisosDto {
    private Integer idTipoPermiso;

    private String nombre;

    private Integer cantDias;

    private String observaciones;

    private Boolean remunerado;

    private Boolean fuerzaMenor;

}
