package com.mobile.base.validation.impl;

import com.mobile.base.constant.BaseConstant;
import com.mobile.base.validation.UserGender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class UserGenderValidation implements ConstraintValidator<UserGender, Integer> {
    private boolean allowNull;
    private static final List<Integer> ALLOWED_VALUES = Arrays.asList(
            BaseConstant.USER_GENDER_MALE,
            BaseConstant.USER_GENDER_FEMALE,
            BaseConstant.USER_GENDER_UNKNOWN
    );

    @Override
    public void initialize(UserGender constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        return ALLOWED_VALUES.contains(value);
    }
}
