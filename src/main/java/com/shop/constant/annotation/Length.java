package com.shop.constant.annotation;

import com.shop.constant.annotation.validator.LengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LengthValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Length {

    String message() default "Length must be {length}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int length() default 0;
}