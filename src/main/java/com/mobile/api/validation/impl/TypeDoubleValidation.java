package com.mobile.api.validation.impl;

import com.mobile.api.validation.TypeDouble;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class TypeDoubleValidation implements ConstraintValidator<TypeDouble, Double> {
    private boolean allowNull;
    private double min;
    private double max;
    private int precision;
    private int scale;
    private String fieldName;

    @Override
    public void initialize(TypeDouble constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        precision = constraintAnnotation.precision();
        scale = constraintAnnotation.scale();
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return allowNull;

        BigDecimal bd = BigDecimal.valueOf(value).stripTrailingZeros();
        int actualPrecision = bd.precision();
        int actualScale = bd.scale();

        if (value < min) {
            return buildViolation(context, fieldName + " must be greater than or equal to " + min);
        }
        if (value > max) {
            return buildViolation(context, fieldName + " must be less than or equal to " + max);
        }
        if (actualPrecision > precision) {
            return buildViolation(context, fieldName + " must not exceed " + precision + " total digits");
        }
        if (actualScale > scale) {
            return buildViolation(context, fieldName + " must not have more than " + scale + " decimal places");
        }

        return true;
    }

    private boolean buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
