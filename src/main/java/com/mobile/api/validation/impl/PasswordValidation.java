package com.mobile.api.validation.impl;

import com.mobile.api.validation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidation implements ConstraintValidator<Password, String> {
    private boolean allowNull;
    private int minLength;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";

    @Override
    public void initialize(Password constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
        minLength = constraintAnnotation.minLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null ? allowNull : value.length() >= minLength && value.matches(PASSWORD_PATTERN);
    }
}
