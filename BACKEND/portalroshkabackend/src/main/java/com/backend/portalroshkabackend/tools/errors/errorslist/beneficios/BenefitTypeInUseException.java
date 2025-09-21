package com.backend.portalroshkabackend.tools.errors.errorslist.beneficios;

public class BenefitTypeInUseException extends RuntimeException{

    public BenefitTypeInUseException(String benefitTypeName){
        super("Existen beneficios asignados al tipo de beneficio " + benefitTypeName + ".");
    }
}
