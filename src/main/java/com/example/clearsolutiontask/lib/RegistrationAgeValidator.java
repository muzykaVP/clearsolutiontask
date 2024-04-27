package com.example.clearsolutiontask.lib;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RegistrationAgeValidator implements
        ConstraintValidator<RegistrationAgeConstraint, LocalDate> {

    @Value("${required.user.registration.age}")
    private Integer validRegistrationAge;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            buildCustomConstraintViolationWithMessage(context, "Birth date is mandatory");
            return false;
        }
        if (value.isAfter(LocalDate.now())) {
            buildCustomConstraintViolationWithMessage(context, "Birth date must be earlier than current date");
            return false;
        }
        if (ChronoUnit.YEARS.between(value, LocalDate.now()) < validRegistrationAge) {
            buildCustomConstraintViolationWithMessage(context, "User must be at least 18 years old");
            return false;
        }
        return true;
    }
    private void buildCustomConstraintViolationWithMessage(ConstraintValidatorContext context, String message){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
