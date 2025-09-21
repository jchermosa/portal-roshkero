package com.backend.portalroshkabackend.tools.errors.errorslist.tecnologias;

public class DuplicateUserTechnologyException extends RuntimeException {
    public DuplicateUserTechnologyException(String username, String technologyName){
        super("El usuario " + username + " ya tiene asignada la tecnologia " + technologyName);
    }
}
