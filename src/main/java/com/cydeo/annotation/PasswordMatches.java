package com.cydeo.annotation;

import com.cydeo.annotation.impl.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })  // Apply at the class level
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Passwords should match.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}