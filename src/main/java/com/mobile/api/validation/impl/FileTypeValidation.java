package com.mobile.api.validation.impl;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.validation.FileType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class FileTypeValidation implements ConstraintValidator<FileType, String> {
    private boolean allowNull;
    private static final Set<String> ALLOWED_VALUES = Set.of(
            BaseConstant.FILE_TYPE_IMAGE,
            BaseConstant.FILE_TYPE_VIDEO,
            BaseConstant.FILE_TYPE_AUDIO,
            BaseConstant.FILE_TYPE_DOCUMENT,
            BaseConstant.FILE_TYPE_ARCHIVE
    );

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null ? allowNull : ALLOWED_VALUES.contains(value);
    }
}
