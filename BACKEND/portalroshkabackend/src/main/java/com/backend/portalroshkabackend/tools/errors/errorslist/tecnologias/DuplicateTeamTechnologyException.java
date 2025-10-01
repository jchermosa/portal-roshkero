package com.backend.portalroshkabackend.tools.errors.errorslist.tecnologias;

public class DuplicateTeamTechnologyException extends RuntimeException {

    public DuplicateTeamTechnologyException(String teamName, String technologyName){
        super("El equipo " + teamName + " ya tiene asignada la tecnologia " + technologyName);
    }
}
