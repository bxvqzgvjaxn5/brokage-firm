package com.brokagefirm.challange;

import java.lang.reflect.Method;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;

import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.CustomerType;

import org.springframework.security.core.GrantedAuthority;


public class AuthHelper {

    public static String getAuthUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_" + CustomerType.ADMIN.name()));
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