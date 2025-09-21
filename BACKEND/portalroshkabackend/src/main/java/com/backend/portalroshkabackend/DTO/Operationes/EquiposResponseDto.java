package com.backend.portalroshkabackend.DTO.Operationes;

import java.sql.Date;
import java.util.List;

import lombok.Data;

import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

@Data
public class EquiposResponseDto {

    private Integer idEquipo;
    private UsuarioisResponseDto lider;
    private List<Tecnologias> tecnologias;
    private String nombre;
    private Date fechaInicio;
    private Date fechaLimite;
    private Clientes cliente;
    private Date fechaCreacion;
    private EstadoActivoInactivo estado;

    // getters & setters
    public UsuarioisResponseDto getLider() {
        return lider;
    }

    public void setLider(UsuarioisResponseDto lider) {
        this.lider = lider;
    }

}
