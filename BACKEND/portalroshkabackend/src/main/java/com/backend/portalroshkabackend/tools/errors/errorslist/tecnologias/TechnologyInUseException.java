package com.backend.portalroshkabackend.tools.errors.errorslist.tecnologias;

public class TechnologyInUseException extends RuntimeException{

    public TechnologyInUseException(String technologyName){
        super("No se puede eliminar la tecnologia " + technologyName + " porque hay usuarios y/o equipos asignados al mismo.");
    }
}
