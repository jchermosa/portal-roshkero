package com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Integer id) {
        super("No se encuentra el dispositivo con ID: " + id);
    }
}
