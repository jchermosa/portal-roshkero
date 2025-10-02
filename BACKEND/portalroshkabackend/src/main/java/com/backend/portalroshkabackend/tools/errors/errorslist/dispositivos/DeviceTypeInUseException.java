package com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos;

public class DeviceTypeInUseException extends RuntimeException{

    public DeviceTypeInUseException(String deviceName){
        super("El dispositivo esta " + deviceName + " esta asignado a usuario/s.");
    }
}
