package com.vinotech.sommelier_api.controller;

import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.service.VinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController // Marque cette classe pour gérer les requêtes REST
@RequestMapping("/api/vins") // Définit l'URL de base pour toutes les méthodes
public class VinController {

    private final VinService vinService;

    // Injection du Service
    public VinController(VinService vinService) {
        this.vinService = vinService;
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
     * Récupère la liste de tous les Vins. Mappé sur GET /api/vins
     */
    @GetMapping
    public List<Vin> getAllVins() {
        return vinService.findAll();
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

}