package com.backend.portalroshkabackend.tools.errors.errorslist.cargos;

public class CargoDuplicateNameException extends RuntimeException{
    public CargoDuplicateNameException(String nombre){
        super("El nombre " + nombre + " ya esta asignado a otro cargo.");
    }
}
