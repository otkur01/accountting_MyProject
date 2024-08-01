package com.cydeo.annotation;

import javax.validation.Payload;

public @interface ValidPassword {
    String message() default "Password should be at least 4 characters long and needs to contain 1 capital letter, 1 small letter, and 1 special character or number.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}