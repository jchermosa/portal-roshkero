package com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos;

public class InvalidFilterValueException extends RuntimeException {
    public InvalidFilterValueException(String filterValue) {
        super("Filter value no es un valor valido:"+ filterValue);
    }
}
