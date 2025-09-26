package com.backend.portalroshkabackend.tools.errors.errorslist.cargos;

public class CargoNotFoundException  extends RuntimeException {
    public CargoNotFoundException(int id) {
        super("Cargo con ID " + id + " no encontrado.");
    }
}