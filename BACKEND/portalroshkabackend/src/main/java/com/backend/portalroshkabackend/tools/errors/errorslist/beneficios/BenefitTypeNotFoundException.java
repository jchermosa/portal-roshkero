package com.backend.portalroshkabackend.tools.errors.errorslist.beneficios;

public class BenefitTypeNotFoundException extends RuntimeException{
    public BenefitTypeNotFoundException(int idTipoBeneficio){
        super("No existe el tipo de beneficio con ID: " + idTipoBeneficio + ".");
    }
}
