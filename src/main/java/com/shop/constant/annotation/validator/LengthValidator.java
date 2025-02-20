package com.shop.constant.annotation.validator;

import com.shop.constant.annotation.Length;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LengthValidator implements ConstraintValidator<Length, String> {

    private int length;

    @Override
    public void initialize(Length constraintAnnotation) {
        this.length = constraintAnnotation.length();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.length() == length;
    }
}
