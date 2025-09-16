package com.backend.portalroshkabackend.tools.errors.errorslist;

public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException(String correo){
        super("EL correo " + correo + " ya esta asignado a otro usuario.");
    }

}
