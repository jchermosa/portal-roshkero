package com.backend.portalroshkabackend.tools.errors.errorslist.user;

public class DuplicateTelefonoException extends RuntimeException {
    public DuplicateTelefonoException(String telefono){
        super("El telefono " + telefono + " ya esta asignado a otro usuario");
    }
}
