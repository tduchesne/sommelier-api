package com.vinotech.sommelier_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        // ON LAISSE TOUT LE MONDE ENTRER (Mode Urgence)
                        .anyRequest().permitAll()
                );

        // DÉSACTIVÉ TEMPORAIREMENT :
        // .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    // DÉSACTIVÉ : On ne vérifie plus la config Clerk au démarrage
    /*
    @Bean
    ApplicationRunner validateClerkConfig(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        return args -> {
            if (issuerUri.contains("placeholder.clerk.dev")) {
                throw new IllegalStateException("...");
            }
        };
    }
    */
}