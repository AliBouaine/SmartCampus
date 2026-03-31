package com.example.Reclamation.service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // na7iw CSRF pour Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/reclamations/**").permitAll() // n5alliw endpoint ma7loul
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
