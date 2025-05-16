package com.mobile.api.validation;

import com.mobile.api.validation.impl.TypeBigDecimalValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TypeBigDecimalValidation.class)
@Documented
public @interface TypeBigDecimal {
    boolean allowNull() default false;

    String fieldName() default "Field";

    String message() default "Invalid BigDecimal value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String min() default "0";

    String max() default "999999999999999.999";

    int scale() default 3;

    boolean exactScale() default false;

    // Bổ sung thêm thuộc tính cho BigDecimal
    int precision() default 18;

    boolean exactPrecision() default false;
}