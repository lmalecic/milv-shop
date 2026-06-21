package com.lmalecic.milvshop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UrlOrPathValidator.class)
public @interface UrlOrPath {
    String message() default "must be a valid URL or a server-relative path";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
