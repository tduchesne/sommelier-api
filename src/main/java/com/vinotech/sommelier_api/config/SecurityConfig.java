package com.vinotech.sommelier_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Désactiver CSRF (inutile pour une API REST Stateless utilisée par mobile)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Gestion des droits d'accès
                .authorizeHttpRequests(auth -> auth
                        // Les endpoints publics (Swagger, Health check si tu en as)
                        .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // TOUT ce qui commence par /api/ demande un Token valide
                        .requestMatchers("/api/**").authenticated()
                        // Le reste est bloqué par précaution
                        .anyRequest().denyAll()
                )

                // 3. Activer le serveur de ressource OAuth2 (Validation JWT via Clerk)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}