package com.brokagefirm.challange.aspects;

import java.lang.annotation.Target;

import com.brokagefirm.challange.models.CustomerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleCheck {
    CustomerType value();
}
