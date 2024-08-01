package com.cydeo.annotation.impl;

import com.cydeo.annotation.ValidPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9@#$%^&+=]).{4,}$"
    );

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
}