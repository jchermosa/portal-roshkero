package com.backend.portalroshkabackend.tools.errors.errorslist.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("Usuario con ID " + id + " no encontrado.");
    }

    public UserNotFoundException(String cedula){
        super("Usuario con cedula numero " + cedula + " no encontrado.");
    }
}