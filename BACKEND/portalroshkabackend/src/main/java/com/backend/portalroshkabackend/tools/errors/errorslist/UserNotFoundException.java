package com.backend.portalroshkabackend.tools.errors.errorslist;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("Usuario con ID " + id + " no encontrado.");
    }
}