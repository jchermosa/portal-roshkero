package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosAllDto {
    private List<UsuarioisResponseDto> usersAll;
}
