package com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos;

public class UniqueSerialException extends RuntimeException {
    public UniqueSerialException(String nroSerie) {
        super("El Nro. de Serie: " + nroSerie + " ya se encuentra registrado en otro dispositivo");
    }
    
}
