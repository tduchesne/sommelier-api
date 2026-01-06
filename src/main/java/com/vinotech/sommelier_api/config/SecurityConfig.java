package com.vinotech.sommelier_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Endpoints publics (Monitoring, Doc)
                        .requestMatchers("/actuator/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 2. Endpoints API protégés (Nécessitent un Token Valide)
                        .requestMatchers("/api/**").authenticated()

                        // 3. Tout le reste est interdit par sécurité
                        .anyRequest().denyAll()
                )
                // 4. Validation du Token JWT via Clerk
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * Validation au démarrage pour s'assurer qu'on a bien l'URL de Clerk.
     * Empêche de démarrer avec la config "placeholder" par erreur.
     */
    @Bean
    ApplicationRunner validateClerkConfig(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        return args -> {
            if (issuerUri.contains("placeholder.clerk.dev")) {
                throw new IllegalStateException(
                        "\n\n🚨 FATAL ERROR: CLERK_ISSUER_URI is not configured! 🚨\n" +
                                "The application is running with the unsafe default placeholder: " + issuerUri + "\n" +
                                "Please set the CLERK_ISSUER_URI environment variable.\n"
                );
            }
        };
    }
}