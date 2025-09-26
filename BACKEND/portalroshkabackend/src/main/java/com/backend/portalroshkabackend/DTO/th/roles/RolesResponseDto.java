package com.backend.portalroshkabackend.DTO.th.roles;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RolesResponseDto {
    private Integer idCargo;

    private String nombre;

    private LocalDateTime fechaCreacion;
}
