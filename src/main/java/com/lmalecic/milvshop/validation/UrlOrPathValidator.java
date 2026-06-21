package com.lmalecic.milvshop.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.UrlValidator;

public class UrlOrPathValidator implements ConstraintValidator<UrlOrPath, String> {

    private static final UrlValidator urlValidator = new UrlValidator();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        if (value.startsWith("/")) {
            return true;
        }
        return urlValidator.isValid(value);
    }
}
