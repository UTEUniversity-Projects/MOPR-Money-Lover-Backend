package com.mobile.api.validation.impl;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.validation.PeriodType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class PeriodTypeValidation implements ConstraintValidator<PeriodType, Integer> {
    private boolean allowNull;
    private static final Set<Integer> ALLOWED_VALUES = Set.of(
            BaseConstant.PERIOD_TYPE_WEEK,
            BaseConstant.PERIOD_TYPE_MONTH,
            BaseConstant.PERIOD_TYPE_QUARTER,
            BaseConstant.PERIOD_TYPE_YEAR,
            BaseConstant.PERIOD_TYPE_CUSTOM
    );

    @Override
    public void initialize(PeriodType constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null ? allowNull : ALLOWED_VALUES.contains(value);
    }
}
