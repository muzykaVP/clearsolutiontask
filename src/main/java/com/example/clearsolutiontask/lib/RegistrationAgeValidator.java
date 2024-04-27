package com.example.clearsolutiontask.lib;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RegistrationAgeValidator implements
        ConstraintValidator<RegistrationAgeConstraint, LocalDate> {

    @Value("${required.user.registration.age}")
    private int validRegistrationAge;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value != null && ChronoUnit.YEARS.between(value, LocalDate.now()) >= validRegistrationAge;
    }
}
