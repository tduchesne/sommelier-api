package com.vinotech.sommelier_api.service;

import com.vinotech.sommelier_api.model.Restaurant;
import com.vinotech.sommelier_api.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RestaurantSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantSecurityService.class);
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantSecurityService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Long getRestaurantIdFromToken(Jwt jwt) {
        String clerkOrgId = jwt.getClaim("org_id");

        if (clerkOrgId == null || clerkOrgId.isEmpty()) {
            logger.warn("Access attempt without organization selection in Clerk context.");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No organization selected.");
        }

        Restaurant restaurant = restaurantRepository.findByClerkId(clerkOrgId)
                .orElseThrow(() -> {
                    logger.error("Security Alert: No restaurant found for Clerk Org ID: {}", clerkOrgId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Invalid organization context.");
                });

        return restaurant.getId();
    }
}