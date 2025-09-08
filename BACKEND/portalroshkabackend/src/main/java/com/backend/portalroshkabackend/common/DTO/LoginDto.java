package com.backend.portalroshkabackend.common.DTO;

public class LoginDto {
    private String correo;
    private String contrasena;
    
    public LoginDto() {
    }
    public LoginDto(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }


    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}