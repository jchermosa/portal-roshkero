package com.backend.portalroshkabackend.DTO.Operationes.Metadatas;

import java.time.LocalDate;

import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Models.Usuario;
import lombok.Data;

@Data
public class UsuariosEquipoResponseDto {
    private Integer idAsignacionUsuarioEquipo;

    private LocalDate fechaEntrada;

    private LocalDate fechaFin;

    private Integer porcentajeTrabajo;

    private Usuario idUsuario;

    private Tecnologias idTecnologia;

    private Equipos equipos;

    private LocalDate fechaCreacion;
}
