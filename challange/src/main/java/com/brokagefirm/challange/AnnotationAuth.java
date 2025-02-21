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
import org.springframework.security.core.GrantedAuthority;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface AuthorizationCheck {
    String value();
}

class AnnotationAuth {
    public static void validate(Object obj) throws Exception {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(AuthorizationCheck.class)) {
                AuthorizationCheck annotation = method.getAnnotation(AuthorizationCheck.class);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) {
                    throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized");
                }
                String role = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden"));
                if (!role.equals(annotation.value())) {
                    throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden");
                }
            }
        }
    }
}