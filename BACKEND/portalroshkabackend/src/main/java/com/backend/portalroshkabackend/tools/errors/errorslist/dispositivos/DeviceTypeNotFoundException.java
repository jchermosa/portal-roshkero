package com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos;

public class DeviceTypeNotFoundException extends RuntimeException {
    public DeviceTypeNotFoundException(Integer id) {
        super("No se encuentra el tipo de dispositivo con el ID:"+ id);
    }
}
