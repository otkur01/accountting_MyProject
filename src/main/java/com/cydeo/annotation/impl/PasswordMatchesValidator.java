package com.cydeo.annotation.impl;

import com.cydeo.annotation.PasswordMatches;
import com.cydeo.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDto> {

    @Override
    public boolean isValid(UserDto dto, ConstraintValidatorContext context) {
        return dto.getPassword().equals(dto.getConfirmPassword());
    }

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
}