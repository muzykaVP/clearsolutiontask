package com.example.clearsolutiontask.lib;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = RegistrationAgeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RegistrationAgeConstraint {
    String message() default "Birth date error occurred";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}