package com.bcol.vtd.api.enviocorreo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(
        validatedBy = {InyeccionSQLValidador.class}
)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InyeccionSQL {
    String message() default "{InyeccionSQL}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
