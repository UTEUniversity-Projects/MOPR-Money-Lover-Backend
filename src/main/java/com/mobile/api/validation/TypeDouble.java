package com.mobile.api.validation;

import com.mobile.api.validation.impl.TypeDoubleValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TypeDoubleValidation.class)
@Documented
public @interface TypeDouble {
    boolean allowNull() default false;

    String fieldName() default "Field";

    String message() default "Invalid double value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    double min() default Double.NEGATIVE_INFINITY;

    double max() default Double.POSITIVE_INFINITY;

    int precision() default 15;

    int scale() default 6;
}
