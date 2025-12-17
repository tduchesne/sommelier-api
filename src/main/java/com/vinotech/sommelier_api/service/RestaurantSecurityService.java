package com.vinotech.sommelier_api.service;

import com.vinotech.sommelier_api.model.Restaurant;
import com.vinotech.sommelier_api.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RestaurantSecurityService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantSecurityService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Extrait l'ID interne du restaurant à partir du Token JWT Clerk.
     * Le token doit contenir la claim 'org_id'.
     */
    public Long getRestaurantIdFromToken(Jwt jwt) {
        // 1. Extraire l'ID de l'organisation Clerk (Standard Clerk B2B)
        String clerkOrgId = jwt.getClaim("org_id");

        if (clerkOrgId == null || clerkOrgId.isEmpty()) {
            // Si l'utilisateur est connecté mais n'a pas sélectionné d'organisation
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Aucune organisation (Restaurant) n'est sélectionnée dans le contexte Clerk.");
        }

        // 2. Trouver le restaurant correspondant en base
        Restaurant restaurant = restaurantRepository.findByClerkId(clerkOrgId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Restaurant non trouvé pour l'ID Clerk : " + clerkOrgId));

        return restaurant.getId();
    }
}