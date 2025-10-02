package com.backend.portalroshkabackend.DTO.th.cargos;

import com.backend.portalroshkabackend.Models.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class CargosResponseDto {
    private Integer idCargo;

    private String nombre;

    private LocalDate fechaCreacion;

}
