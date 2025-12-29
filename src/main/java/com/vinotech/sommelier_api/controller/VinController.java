package com.vinotech.sommelier_api.controller;

import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.service.RestaurantSecurityService;
import com.vinotech.sommelier_api.service.VinService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController // Marque cette classe pour gérer les requêtes REST
@RequestMapping("/api/vins") // Définit l'URL de base pour toutes les méthodes
public class VinController {

    private final VinService vinService;
    private final RestaurantSecurityService securityService;

    // Injection du Service
    public VinController(VinService vinService, RestaurantSecurityService securityService) {
        this.vinService = vinService;
        this.securityService = securityService;
    }

    /**
     * Crée un nouveau Vin. Mappé sur POST /api/vins
     */
    @PostMapping
    public ResponseEntity<Vin> createVin(@RequestBody Vin vin) {
        Vin saved = vinService.save(vin);
        URI location = URI.create("/api/vins/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Récupère un Vin par son ID. Mappé sur GET /api/vins/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vin> getVinById(@PathVariable Long id) {
        return vinService.findById(id)
                .map(ResponseEntity::ok) // Si trouvé (200 OK)
                .orElse(ResponseEntity.notFound().build()); // Si non trouvé (404 Not Found)
    }

    /**
     * Recherche filtrée.
     * URL: GET /api/vins?couleur=ROUGE&minPrix=50&maxPrix=100&region=loire&search=...
     * Tous les paramètres sont optionnels.
     */
    @GetMapping
    public List<Vin> searchVins(
            // @AuthenticationPrincipal Jwt jwt, // <--- DÉSACTIVÉ
            @RequestParam(required = false) BigDecimal minPrix,
            @RequestParam(required = false) BigDecimal maxPrix,
            @RequestParam(required = false) CouleurVin couleur,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String search
    ) {
        // DÉSACTIVÉ : Récupération dynamique
        // Long restaurantId = securityService.getRestaurantIdFromToken(jwt);

        // ACTIVÉ : Mode "Single Restaurant" (Urgence)
        Long restaurantId = 1L;

        return vinService.searchVins(restaurantId, minPrix, maxPrix, couleur, region, search);
    }
}