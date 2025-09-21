package com.backend.portalroshkabackend.tools.errors.errorslist.roles;

public class RolesNotFoundException  extends RuntimeException {
    public RolesNotFoundException(int id) {
        super("Rol con ID " + id + " no encontrado.");
    }
}