package com.backend.portalroshkabackend.common.DTO;

public class ResponseLoginDto {
    private String correo;
    private String token;
    private String rol;

    public ResponseLoginDto() {
    }




    public ResponseLoginDto(String correo, String token) {
        this.correo = correo;
        this.token = token;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
