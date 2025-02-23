package com.brokagefirm.challange.aspects;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.brokagefirm.challange.models.CustomerType;

@Aspect
@Component
public class RoleCheckAspect {
    @Before("@annotation(RoleCheck)")
    public void checkUserRole(org.aspectj.lang.JoinPoint joinPoint){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RoleCheck roleCheck = method.getAnnotation(RoleCheck.class);

        if (roleCheck != null) {
            CustomerType requiredRole = roleCheck.value();
            
            boolean hasRole = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + requiredRole.name()));

            if (!hasRole) {
                throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden");
            }
        }
    }
}
