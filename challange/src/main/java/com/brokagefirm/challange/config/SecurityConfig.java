package com.brokagefirm.challange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.brokagefirm.challange.repositories.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomerRepository customerRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> customerRepository.findByEmail(username)
                .map(customer -> User.withUsername(customer.getEmail())
                        .password(customer.getPassword())
                        .roles(customer.getType().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html","/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(httpBasic -> {
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
