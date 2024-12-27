package com.api.meetudy.global.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum> {

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        return value != null;
    }

}