package com.backend.portalroshkabackend.tools.errors.errorslist.permisos;

public class PermissionTypeInUseException extends RuntimeException{

    public PermissionTypeInUseException(String permissionTypeName){
        super("Existen permisos asignados al tipo de permiso " + permissionTypeName + ".");
    }
}
