package com.shop.constant.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Pattern(regexp = "^[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]{1,255}$", message = "Email pattern does not match")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Email {

    String message() default "Invalid input";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}