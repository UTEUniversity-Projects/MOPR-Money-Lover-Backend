package com.mobile.api.validation.impl;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.validation.UserGender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class UserGenderValidation implements ConstraintValidator<UserGender, Integer> {
    private boolean allowNull;
    private static final Set<Integer> ALLOWED_VALUES = Set.of(
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
        return value == null ? allowNull : ALLOWED_VALUES.contains(value);
    }
}
