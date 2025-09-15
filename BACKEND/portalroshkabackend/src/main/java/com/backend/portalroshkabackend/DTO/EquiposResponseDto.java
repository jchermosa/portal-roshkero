package com.backend.portalroshkabackend.DTO;

public class EquiposResponseDto {

    private Integer idEquipo;
    private String nombre;

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}