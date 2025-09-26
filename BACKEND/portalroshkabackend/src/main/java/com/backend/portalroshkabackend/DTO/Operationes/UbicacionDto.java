package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDto {
    private Integer idUbicacion;
    private String nombre;        // название локации
    private Integer idDiaLaboral; // id дня
    private String dia;           // название дня
}