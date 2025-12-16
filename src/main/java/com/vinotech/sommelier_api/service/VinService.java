package com.vinotech.sommelier_api.service;
import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.VinRepository;
import org.springframework.stereotype.Service;
import com.vinotech.sommelier_api.repository.specs.VinSpecification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VinService {

    private final VinRepository vinRepository;

    public VinService(VinRepository vinRepository) {
        this.vinRepository = vinRepository;
    }

    public Vin save(Vin vin) {
        return vinRepository.save(vin);
    }

    public List<Vin> findAll() {
        return vinRepository.findAll();
    }

    public Optional<Vin> findById(Long id) {
        return vinRepository.findById(id);
    }

    public List<Vin> searchVins(Long restaurantId, BigDecimal minPrix, BigDecimal maxPrix, CouleurVin couleur, String region, String search) {
        // Toute la complexité est déléguée à la Specification
        VinSpecification spec = new VinSpecification(restaurantId, minPrix, maxPrix, couleur, region, search);

        // Grâce à "JpaSpecificationExecutor", findAll accepte maintenant 'spec'
        return vinRepository.findAll(spec);
    }
}