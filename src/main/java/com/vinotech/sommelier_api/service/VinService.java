package com.vinotech.sommelier_api.service;

import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.VinRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // Indique à Spring que c'est une classe de logique métier
public class VinService {

    private final VinRepository vinRepository;

    // Injection de Dépendance : Spring fournit automatiquement l'implémentation du Repository
    public VinService(VinRepository vinRepository) {
        this.vinRepository = vinRepository;
    }

    /**
     * Enregistre ou met à jour un vin dans la base de données.
     */
    public Vin save(Vin vin) {
        // Le Repository traduit l'opération en SQL (INSERT ou UPDATE)
        return vinRepository.save(vin);
    }

    /**
     * Récupère tous les vins.
     */
    public List<Vin> findAll() {
        return vinRepository.findAll();
    }

    /**
     * Récupère un vin par son ID.
     */
    public Optional<Vin> findById(Long id) {
        return vinRepository.findById(id);
    }
}