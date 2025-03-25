package com.mobile.api.validation.impl;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.validation.AccountKind;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class AccountKindValidation implements ConstraintValidator<AccountKind, Integer> {
    private boolean allowNull;
    private static final List<Integer> ALLOWED_VALUES = Arrays.asList(
            BaseConstant.USER_KIND_ADMIN,
            BaseConstant.USER_KIND_MANAGER,
            BaseConstant.USER_KIND_USER
    );

    @Override
    public void initialize(AccountKind constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null ? allowNull : ALLOWED_VALUES.contains(value);
    }
}
