package com.shop.constant.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "PhoneNumber pattern does not match")
@Size(max = 11, message = "PhoneNumber must be within {max} characters or less")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PhoneNumber {

    String message() default "Invalid input";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
