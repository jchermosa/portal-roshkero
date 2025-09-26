package com.backend.portalroshkabackend.DTO.Operationes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull; // Для POST обязательных полей
import lombok.Data;

import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.ClientesResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

@Data
public class EquiposResponseDto {

    private Integer idEquipo;
    private UsuarioisResponseDto lider;
    private List<TecnologiasDto> tecnologias;

    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaLimite;
    private ClientesResponseDto cliente;
    private List<EquipoDiaUbicacionResponceDto> equipoDiaUbicacion;
    private LocalDateTime fechaCreacion;
    private EstadoActivoInactivo estado;
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
