package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.Data;

import java.time.LocalDate;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Models.Usuario;

@Data
public class AsignacionResponseDto {
    private Integer IdAsignacionUsuarioEquipo;
    private LocalDate fechaEntrada;
    private LocalDate fechaFin;
    private Integer porcentajeTrabajo;
    private Usuario idUsuario; 
    private Tecnologias idTecnologia;
    private Equipos equipo;
    private LocalDate fechaCreacion;

    public AsignacionResponseDto(){
        
    }


}
