package com.backend.portalroshkabackend.tools.errors.errorslist.solicitudDispositivos;

public class AlreadyCheckedRequestException extends RuntimeException {
    public AlreadyCheckedRequestException(Integer ID) {
        super("La solicitud ya fue procesada.");
    }
}
