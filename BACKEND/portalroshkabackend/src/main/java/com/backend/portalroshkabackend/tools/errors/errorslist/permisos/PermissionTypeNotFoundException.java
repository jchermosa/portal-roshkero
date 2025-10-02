package com.backend.portalroshkabackend.tools.errors.errorslist.permisos;

public class PermissionTypeNotFoundException extends RuntimeException{
    public PermissionTypeNotFoundException(int idTipoPermiso){
        super("No existe el tipo de permiso con ID: " + idTipoPermiso + ".");
    }
}
