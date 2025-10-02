package com.backend.portalroshkabackend.tools.errors.errorslist.solicitudDispositivos;

public class DeviceRequestProcessingException extends RuntimeException {
    public DeviceRequestProcessingException(String message) {
        super("Error al procesar la solicitud "+ message);
    }
}
