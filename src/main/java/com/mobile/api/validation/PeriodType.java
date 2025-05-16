package com.mobile.api.validation;

import com.mobile.api.validation.impl.PeriodTypeValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeriodTypeValidation.class)
@Documented
public @interface PeriodType {
    boolean allowNull() default false;

    String message() default "Invalid period type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
