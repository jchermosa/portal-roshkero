package com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Integer id) {
        super("Ubicacion con id:"+id+" no existe");
    }
}
