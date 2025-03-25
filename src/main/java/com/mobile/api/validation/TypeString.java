package com.mobile.api.validation;

import com.mobile.api.validation.impl.TypeStringValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TypeStringValidation.class)
@Documented
public @interface TypeString {
    boolean allowNull() default false;

    int maxlength() default 255;

    String fieldName() default "Field";

    String message() default "Invalid string value";

    String messageNull() default "{fieldName} must not be null";
    String messageEmpty() default "{fieldName} must not be empty";
    String messageMaxLength() default "{fieldName} exceeds max length of {maxLength} characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
