package com.backend.portalroshkabackend.Exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EstadoValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEstado {
    String message() default "Solo se permite A o I";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}   