package com.backend.portalroshkabackend.DTO.Operationes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.ClientesResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

@Data
public class EquiposResponseDto {

    private Integer idEquipo;
    private UsuarioisResponseDto lider;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaLimite;
    private ClientesResponseDto cliente;
    private LocalDateTime fechaCreacion;

    private EstadoActivoInactivo estado;
    private List<TecnologiasDto> tecnologias;
    private List<EquipoDiaUbicacionResponceDto> equipoDiaUbicacion;
    private List<UsuarioisResponseDto> usuarios;
    private List<UsuarioAsignacionDto> usuariosAsignacion;
    private List<UsuarioisResponseDto> usuariosNoEnEquipo;
    // getters & setters
    public UsuarioisResponseDto getLider() {
        return lider;
    }

    public void setLider(UsuarioisResponseDto lider) {
        this.lider = lider;
    }

}
