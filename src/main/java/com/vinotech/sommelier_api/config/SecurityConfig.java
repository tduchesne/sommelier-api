package com.vinotech.sommelier_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;

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
                        // Les endpoints publics (Swagger, Health check)
                        .requestMatchers("/actuator/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // TOUT ce qui commence par /api/ demande un Token valide
                        .requestMatchers("/api/**").authenticated()
                        // Le reste est bloqué par précaution
                        .anyRequest().denyAll()
                )

                // 3. Activer le serveur de ressource OAuth2 (Validation JWT via Clerk)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
    /**
     * Fail fast at startup if Clerk configuration is missing.
     * Prevents deploying with the default placeholder URL.
     */
    @Bean
    ApplicationRunner validateClerkConfig(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri)
    {
        return args -> {
            if (issuerUri.contains("placeholder.clerk.dev")) {
                throw new IllegalStateException(
                        "\n\n🚨 FATAL ERROR: CLERK_ISSUER_URI is not configured! 🚨\n" +
                                "The application is running with the unsafe default placeholder: " + issuerUri + "\n" +
                                "Please set the CLERK_ISSUER_URI environment variable in your run configuration or Render dashboard.\n"
                );
            }
        };
    }
}