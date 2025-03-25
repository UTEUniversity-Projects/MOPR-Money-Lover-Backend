package com.mobile.api.validation;

import com.mobile.api.validation.impl.EmailAddressValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailAddressValidation.class)
@Documented
public @interface EmailAddress {
    boolean allowNull() default false;

    String message() default "Invalid email format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
