package com.backend.portalroshkabackend.tools.errors.errorslist.solicitudDispositivos;

public class DeviceRequestNotFoundException extends RuntimeException {
    public DeviceRequestNotFoundException(Integer id) {
        super("No se encontro una solicitud de Dispositivo con el id: "+ id);
    }
}
