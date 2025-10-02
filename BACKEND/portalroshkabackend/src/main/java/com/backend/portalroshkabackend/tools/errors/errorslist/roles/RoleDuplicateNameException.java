package com.backend.portalroshkabackend.tools.errors.errorslist.roles;

public class RoleDuplicateNameException extends RuntimeException{
    public RoleDuplicateNameException(String nombre){
        super("El nombre " + " ya esta asignado a otro rol.");
    }
}
