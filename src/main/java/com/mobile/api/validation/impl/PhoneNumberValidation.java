package com.mobile.api.validation.impl;

import com.mobile.api.validation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class PhoneNumberValidation implements ConstraintValidator<PhoneNumber, String> {
    private boolean allowNull;
    private static final String PHONE_PATTERN = "^[0-9]{10,15}$";

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null ? allowNull : StringUtils.isNoneBlank(value) && value.matches(PHONE_PATTERN);
    }
}
