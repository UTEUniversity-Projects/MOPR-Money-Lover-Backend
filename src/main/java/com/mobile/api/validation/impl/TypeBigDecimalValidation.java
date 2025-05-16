package com.mobile.api.validation.impl;

import com.mobile.api.validation.TypeBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class TypeBigDecimalValidation implements ConstraintValidator<TypeBigDecimal, BigDecimal> {
    private boolean allowNull;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String fieldName;
    private int scale;
    private boolean exactScale;
    private int precision;
    private boolean exactPrecision;

    @Override
    public void initialize(TypeBigDecimal constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        this.fieldName = constraintAnnotation.fieldName();
        this.minValue = new BigDecimal(constraintAnnotation.min());
        this.maxValue = new BigDecimal(constraintAnnotation.max());
        this.scale = constraintAnnotation.scale();
        this.exactScale = constraintAnnotation.exactScale();
        this.precision = constraintAnnotation.precision();
        this.exactPrecision = constraintAnnotation.exactPrecision();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }

        // Check minimum values
        if (value.compareTo(minValue) < 0) {
            return buildViolation(
                    context,
                    String.format("%s must be greater than or equal to %s", fieldName, minValue.toPlainString())
            );
        }

        // Check maximum value
        if (value.compareTo(maxValue) > 0) {
            return buildViolation(
                    context,
                    String.format("%s must be less than or equal to %s", fieldName, maxValue.toPlainString())
            );
        }

        // Check scale
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

        // Check precision
        int actualPrecision = value.precision();
        if (exactPrecision && actualPrecision != precision) {
            return buildViolation(
                    context,
                    String.format("%s must have exactly %d total digits", fieldName, precision)
            );
        } else if (!exactPrecision && actualPrecision > precision) {
            return buildViolation(
                    context,
                    String.format("%s must have at most %d total digits", fieldName, precision)
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