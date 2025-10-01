package com.backend.portalroshkabackend.tools.errors.errorslist;

public class UserAlreadyInactiveException extends RuntimeException{
    public UserAlreadyInactiveException(int id){
        super("El usuario con ID " + id + " ya se encuentra inactivo");
    }
}
