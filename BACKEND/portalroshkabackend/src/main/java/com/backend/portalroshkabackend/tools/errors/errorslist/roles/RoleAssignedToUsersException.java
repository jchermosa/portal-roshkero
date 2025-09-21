package com.backend.portalroshkabackend.tools.errors.errorslist.roles;

public class RoleAssignedToUsersException extends RuntimeException{

    public RoleAssignedToUsersException(String nombreRol, Integer cantidad){
        super("No se puede eliminar el rol " + " porque esta asignado a " + cantidad + " usuarios.");
    }
}
