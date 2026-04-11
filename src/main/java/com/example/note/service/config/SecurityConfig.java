        package com.example.note.service.config;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.web.SecurityFilterChain;

        @Configuration
        public class SecurityConfig {

            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                        .csrf(csrf -> csrf.disable())
                        .cors(cors -> cors.disable())           // optionnel pour le moment
                        .authorizeHttpRequests(auth -> auth
                                // Autorise tous les endpoints du bulletin et des notes
                                .requestMatchers("/api/bulletin/**").permitAll()
                                .requestMatchers("/notes/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()   // pour les health checks
                                .anyRequest().authenticated()
                        )
                        // Désactive explicitement le Resource Server OAuth2 (sinon il force le JWT)
                        .oauth2ResourceServer(oauth2 -> oauth2.disable());

                return http.build();
            }
        }