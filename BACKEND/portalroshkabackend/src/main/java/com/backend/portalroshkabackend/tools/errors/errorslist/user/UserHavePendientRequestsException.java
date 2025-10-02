package com.backend.portalroshkabackend.tools.errors.errorslist.user;

public class UserHavePendientRequestsException extends RuntimeException{
    public UserHavePendientRequestsException(String nombre, String apellido){
        super("El usuario " + nombre + " " + apellido + " tiene solicitudes pendientes.");
    }
}
