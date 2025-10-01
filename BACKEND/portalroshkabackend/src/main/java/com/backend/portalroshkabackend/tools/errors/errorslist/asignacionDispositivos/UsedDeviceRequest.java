package com.backend.portalroshkabackend.tools.errors.errorslist.asignacionDispositivos;

public class UsedDeviceRequest extends RuntimeException {
    public UsedDeviceRequest(Integer id) {
        super("La solicitud con id:"+ id +" ya ha sido relacionada con una asignacion" );
    }
}
