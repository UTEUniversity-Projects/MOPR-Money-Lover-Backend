package com.mobile.api.validation.impl;

import com.mobile.api.validation.EmailAddress;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class EmailAddressValidation implements ConstraintValidator<EmailAddress, String> {
    private boolean allowNull;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    @Override
    public void initialize(EmailAddress constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null ? allowNull : StringUtils.isNoneBlank(value) && value.matches(EMAIL_REGEX);
    }
}
