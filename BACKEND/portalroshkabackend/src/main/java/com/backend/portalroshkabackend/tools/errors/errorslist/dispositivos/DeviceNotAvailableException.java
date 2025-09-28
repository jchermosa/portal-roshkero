package com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos;

//Usar en caso de que el dispositivo ya este en uso o no se encuentre disponible
public class DeviceNotAvailableException extends RuntimeException {
    public DeviceNotAvailableException(Integer id) {
        super("El dispositivo con el Id:" + id + " no se encuentra disponible");
    }
}
