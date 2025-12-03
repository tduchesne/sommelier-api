package com.vinotech.sommelier_api.service;

import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.VinRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    /**
     * Recherche avancée avec critères dynamiques et pagination.
     */
    public Page<Vin> searchVins(CouleurVin couleur, Double minPrix, Double maxPrix, String region, Pageable pageable) {
        Specification<Vin> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtre Couleur
            if (couleur != null) {
                predicates.add(criteriaBuilder.equal(root.get("couleur"), couleur));
            }

            // 2. Filtre Prix Min (Conversion Double -> BigDecimal)
            if (minPrix != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("prix"), BigDecimal.valueOf(minPrix)));
            }

            // 3. Filtre Prix Max (Conversion Double -> BigDecimal)
            if (maxPrix != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("prix"), BigDecimal.valueOf(maxPrix)));
            }

            // 4. Filtre Région
            if (region != null && !region.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("region")),
                        "%" + region.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return vinRepository.findAll(spec, pageable);
    }
}