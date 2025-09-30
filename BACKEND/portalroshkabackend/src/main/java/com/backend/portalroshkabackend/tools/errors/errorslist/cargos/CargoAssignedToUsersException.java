package com.backend.portalroshkabackend.tools.errors.errorslist.cargos;

public class CargoAssignedToUsersException extends RuntimeException{
    public CargoAssignedToUsersException(String cargoName){
        super("No se puede elimnar el cargo " + cargoName + " porque tiene empleados asignados.");
    }
}
