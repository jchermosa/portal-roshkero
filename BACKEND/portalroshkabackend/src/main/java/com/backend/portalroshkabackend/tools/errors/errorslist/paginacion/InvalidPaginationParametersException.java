package com.backend.portalroshkabackend.tools.errors.errorslist.paginacion;

public class InvalidPaginationParametersException extends RuntimeException {
    public InvalidPaginationParametersException(String message) {
        super("Parámetros de paginación inválidos: " + message);
    }
}
