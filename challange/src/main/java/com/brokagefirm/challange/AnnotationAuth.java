package com.brokagefirm.challange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;

import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.CustomerType;

import org.springframework.security.core.GrantedAuthority;


public class AnnotationAuth {
    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(CustomerType.ADMIN.name()));
    }

    public static void validate(Object obj) throws Exception {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(AnnotationAuthCheck.class)) {
                AnnotationAuthCheck annotation = method.getAnnotation(AnnotationAuthCheck.class);
                if (annotation.value() == CustomerType.ADMIN && !isAdmin()) {
                    throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden");
                }
            }
        }
    }

    public static void validateCustomer(Customer expectedCustomer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (isAdmin()) {
            return;
        }

        if (!authentication.getName().equals(expectedCustomer.getEmail())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden");
        }
    }
}