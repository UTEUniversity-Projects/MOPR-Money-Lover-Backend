package com.mobile.base.validation;

import com.mobile.base.validation.impl.GroupKindValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GroupKindValidation.class)
@Documented
public @interface GroupKind {
    boolean allowNull() default false;

    String message() default "Invalid group kind";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
