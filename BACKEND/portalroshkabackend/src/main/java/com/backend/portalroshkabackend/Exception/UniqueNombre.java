package com.backend.portalroshkabackend.Exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NombreUnicoValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueNombre {
    String message() default "El nombre ya existe";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
