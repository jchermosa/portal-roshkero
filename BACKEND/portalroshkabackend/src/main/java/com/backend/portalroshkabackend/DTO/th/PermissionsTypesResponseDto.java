package com.backend.portalroshkabackend.DTO.th;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissionsTypesResponseDto {

    private Integer idPermiso;

    private String nombre;

    private Integer cantDias;
}
