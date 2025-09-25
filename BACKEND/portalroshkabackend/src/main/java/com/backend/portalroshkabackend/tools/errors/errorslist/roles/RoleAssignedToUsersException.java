package com.backend.portalroshkabackend.tools.errors.errorslist.roles;

public class RoleAssignedToUsersException extends RuntimeException{

    public RoleAssignedToUsersException(String nombreRol){
        super("No se puede eliminar el rol " + nombreRol + " porque tiene empleados asignados");
    }
}
