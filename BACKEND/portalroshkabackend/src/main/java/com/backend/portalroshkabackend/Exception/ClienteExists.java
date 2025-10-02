package com.backend.portalroshkabackend.Exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ClienteExistsValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClienteExists {
    String message() default "El cliente no existe";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}