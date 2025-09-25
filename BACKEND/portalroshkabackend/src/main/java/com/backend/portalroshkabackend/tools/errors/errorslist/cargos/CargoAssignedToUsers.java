package com.backend.portalroshkabackend.tools.errors.errorslist.cargos;

public class CargoAssignedToUsers extends RuntimeException{
    public CargoAssignedToUsers(String cargoName){
        super("No se puede elimnar el cargo " + cargoName + " porque tiene empleados asignados.");
    }
}
