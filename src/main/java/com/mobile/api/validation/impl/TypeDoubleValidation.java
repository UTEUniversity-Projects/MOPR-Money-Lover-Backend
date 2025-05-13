package com.mobile.api.validation.impl;

import com.mobile.api.validation.TypeDouble;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class TypeDoubleValidation implements ConstraintValidator<TypeDouble, BigDecimal> {
    private boolean allowNull;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String fieldName;
    private int scale;
    private boolean exactScale;

    @Override
    public void initialize(TypeDouble constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        this.fieldName = constraintAnnotation.fieldName();
        this.minValue = BigDecimal.valueOf(constraintAnnotation.min());
        this.maxValue = BigDecimal.valueOf(constraintAnnotation.max());
        this.scale = constraintAnnotation.scale();
        this.exactScale = constraintAnnotation.exactScale();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }

        if (value.compareTo(minValue) < 0) {
            return buildViolation(
                    context,
                    String.format("%s must be greater than %s", fieldName, minValue.toPlainString())
            );
        }
        if (value.compareTo(maxValue) > 0) {
            return buildViolation(
                    context,
                    String.format("%s must be less than %s", fieldName, maxValue.toPlainString())
            );
        }

        int actualScale = value.scale();
        if (exactScale && actualScale != scale) {
            return buildViolation(
                    context,
                    String.format("%s must have exactly %d decimal places", fieldName, scale)
            );
        } else if (!exactScale && actualScale > scale) {
            return buildViolation(
                    context,
                    String.format("%s must have at most %d decimal places", fieldName, scale)
            );
        }

        return true;
    }

    private boolean buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
}