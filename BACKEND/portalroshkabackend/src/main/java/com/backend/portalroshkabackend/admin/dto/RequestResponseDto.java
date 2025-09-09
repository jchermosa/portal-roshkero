package com.backend.portalroshkabackend.admin.dto;

public class RequestResponseDto {
    
    private int id_request;
    private String nombre;

    public int getId_request() {
        return id_request;
    }

    public void setId_request(int id_request) {
        this.id_request = id_request;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
