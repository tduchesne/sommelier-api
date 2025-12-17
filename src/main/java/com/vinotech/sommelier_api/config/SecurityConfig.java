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
                // 1. Deactivate CSRF and set session management to stateless
                .csrf(AbstractHttpConfigurer::disable)

                // Session management: stateless pour OAuth2 Resource Server
                .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))

                // 2. Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Authorize public endpoints
                        .requestMatchers("/actuator/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Secure API endpoints
                        .requestMatchers("/api/**").authenticated()
                        // Deny all other requests
                        .anyRequest().denyAll()
                )

                // 3. Configure OAuth2 Resource Server with JWT
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
            if (issuerUri == null || issuerUri.isEmpty() || issuerUri.equals("https://placeholder.clerk.dev")) {
                throw new IllegalStateException(
                        "\n\n🚨 FATAL ERROR: CLERK_ISSUER_URI is not configured! 🚨\n" +
                                "The application is running with the unsafe default placeholder: " + issuerUri + "\n" +
                                "Please set the CLERK_ISSUER_URI environment variable in your run configuration or Render dashboard.\n"
                );
            }
        };
    }
}