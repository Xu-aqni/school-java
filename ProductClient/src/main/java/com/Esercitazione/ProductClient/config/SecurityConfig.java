package com.Esercitazione.ProductClient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/products", "/products/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/products", true)
                .permitAll()
            );

        return http.build();
    }
}