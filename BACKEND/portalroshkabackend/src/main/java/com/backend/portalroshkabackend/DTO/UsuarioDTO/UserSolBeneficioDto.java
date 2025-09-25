package com.backend.portalroshkabackend.DTO.UsuarioDTO;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSolBeneficioDto {

    private Integer id_tipo_beneficio;
    private LocalDate fecha_inicio;
    private Integer cant_dias;
    private String comentario;
    private Integer monto;

}
