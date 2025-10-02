package com.backend.portalroshkabackend.tools.errors.errorslist.user;

public class UserAlreadyInactiveException extends RuntimeException{
    public UserAlreadyInactiveException(String nombre, String apellido){
        super("El usuario " + nombre.trim() + " " + apellido.trim() + " ya se encuentra inactivo");
    }
}
