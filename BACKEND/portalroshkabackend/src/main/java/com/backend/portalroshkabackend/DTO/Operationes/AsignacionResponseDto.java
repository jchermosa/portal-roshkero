package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.Data;
import java.sql.Date;

import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Models.Usuario;

@Data
public class AsignacionResponseDto {
    private Integer idAsignacionUsuario;
    private Date fechaEntrada;
    private Date fechaFin;
    private Float porcentajeTrabajo;
    private Usuario idUsuario; 
    private Tecnologias idTecnologia;
    private Equipos equipo;
    private Date fechaCreacion;

    public AsignacionResponseDto(){
        
    }


}
