package com.mobile.base.validation.impl;

import com.mobile.base.validation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidation implements ConstraintValidator<PhoneNumber, String> {
    private boolean allowNull;
    private static final String PHONE_PATTERN = "^[0-9]{10,15}$";

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        return !value.trim().isEmpty() && value.matches(PHONE_PATTERN);
    }
}
