package com.backend.portalroshkabackend.tools.errors.errorslist.cargos;

public class CargoAssignedToUsers extends RuntimeException{
    public CargoAssignedToUsers(String cargoName){
        super("El cargo " + cargoName + " tiene empleados asociados.");
    }
}
