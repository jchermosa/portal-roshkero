package com.backend.portalroshkabackend.tools.errors.errorslist.user;

public class UserAlreadyInactiveException extends RuntimeException{
    public UserAlreadyInactiveException(String nombreApellido){
        super("El usuario " + nombreApellido + " ya se encuentra inactivo");
    }
}
