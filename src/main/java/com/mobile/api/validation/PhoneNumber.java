package com.mobile.api.validation;

import com.mobile.api.validation.impl.PhoneNumberValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidation.class)
@Documented
public @interface PhoneNumber {
    boolean allowNull() default false;

    String message() default "Phone number must be 10 to 15 digits.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
