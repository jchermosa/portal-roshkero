package com.backend.portalroshkabackend.tools.errors.errorslist.solicitudDispositivos;

public class InvalidRequestStateException extends RuntimeException {
    public InvalidRequestStateException(String estado) {
        super("Tipo de estado invalido para una solicitud " + estado);
    }
}
