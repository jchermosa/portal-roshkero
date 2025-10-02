package com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos;

public class DeviceMappingException extends RuntimeException {
    public DeviceMappingException(String message, Throwable cause) {
        super("Error al convertir dispositivos a DTO: " + message, cause);
    }
}
