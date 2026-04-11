package com.example.Reservation.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // na7iw CSRF pour Postman
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/reservations/**").permitAll() // n5alliw endpoint ma7loul
//                        .anyRequest().authenticated()
//                );
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()   // ← AJOUT OBLIGATOIRE
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
