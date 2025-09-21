package com.backend.portalroshkabackend.tools.errors.errorslist.user;

public class DuplicateCedulaException extends RuntimeException{
    public DuplicateCedulaException(int nroCedula){
        super("El numero de cedula " + nroCedula + " ya esta asignado a otro usuario");
    }
}
