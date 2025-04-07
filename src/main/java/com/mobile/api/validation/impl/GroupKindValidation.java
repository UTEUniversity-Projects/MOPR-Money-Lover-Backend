package com.mobile.api.validation.impl;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.validation.GroupKind;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class GroupKindValidation implements ConstraintValidator<GroupKind, Integer> {
    private boolean allowNull;
    private static final Set<Integer> ALLOWED_VALUES = Set.of(
            BaseConstant.GROUP_KIND_ADMIN,
            BaseConstant.GROUP_KIND_MANAGER,
            BaseConstant.GROUP_KIND_INTERNAL,
            BaseConstant.GROUP_KIND_USER
    );

    @Override
    public void initialize(GroupKind constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null ? allowNull : ALLOWED_VALUES.contains(value);
    }
}
