package com.shop.constant.annotation.validator;

import com.shop.constant.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            setErrorMessage(context, "비밀번호는 필수 입력값입니다.");
            return false;
        }

        if (password.length() < 8 || password.length() > 20) {
            setErrorMessage(context, "비밀번호는 8~20자여야 합니다.");
            return false;
        }

        boolean isLetter = false;
        boolean isDigit = false;
        boolean isSpecialChar = false;
        boolean isKorean = false;
        String specialCharacters = "!@#$%^&*()_+-=[]{};':\"\\|,.<>/?~%";

        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) {
                isLetter = true;
            } else if (Character.isDigit(ch)) {
                isDigit = true;
            } else if (specialCharacters.contains(String.valueOf(ch))) {
                isSpecialChar = true;
            } else if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.HANGUL_SYLLABLES ||
                    Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.HANGUL_JAMO ||
                    Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
                isKorean = true; // 🚨 한글 포함 확인
            }
        }

        if (!isLetter) {
            setErrorMessage(context, "비밀번호에는 최소 1개의 영문자가 포함되어야 합니다.");
            return false;
        }

        if (!isDigit) {
            setErrorMessage(context, "비밀번호에는 최소 1개의 숫자가 포함되어야 합니다.");
            return false;
        }

        if (!isSpecialChar) {
            setErrorMessage(context, "비밀번호에는 최소 1개의 특수문자가 포함되어야 합니다.");
            return false;
        }

        if (isKorean) {
            setErrorMessage(context, "비밀번호에 한글을 포함할 수 없습니다.");
            return false;
        }

        return true;
    }

    private void setErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
