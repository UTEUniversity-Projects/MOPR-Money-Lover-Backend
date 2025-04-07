package com.mobile.api.validation;

import com.mobile.api.validation.impl.FileTypeValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileTypeValidation.class)
@Documented
public @interface FileType {
    boolean allowNull() default false;

    String message() default "Invalid file type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
