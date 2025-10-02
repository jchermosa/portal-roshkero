package com.backend.portalroshkabackend.DTO.UsuarioDTO;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class UserSolVacacionDto {

    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private String comentario;
    private String Destinatario; // Puede ser "Talento Humano" o el nombre del Team Leader
    private Integer cantidadDias;

}
