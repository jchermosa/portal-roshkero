package com.backend.portalroshkabackend.tools.errors.errorslist;

public class CargoAlreadyInactiveException extends RuntimeException{
    public CargoAlreadyInactiveException(int id){
        super("El cargo con ID " + id + " ya se encuentra inactivo");
    }
}
