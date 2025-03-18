package com.mobile.base.validation;

import com.mobile.base.validation.impl.AccountKindValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountKindValidation.class)
@Documented
public @interface AccountKind {
    boolean allowNull() default false;

    String message() default "Invalid account kind";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
