package com.vinotech.sommelier_api.repository.specs;

import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VinSpecification implements Specification<Vin> {

    private final Long restaurantId;
    private final BigDecimal minPrix;
    private final BigDecimal maxPrix;
    private final CouleurVin couleur;
    private final String region;
    private final String search;

    public VinSpecification(Long restaurantId, BigDecimal minPrix, BigDecimal maxPrix, CouleurVin couleur, String region, String search) {
        this.restaurantId = restaurantId;
        this.minPrix = minPrix;
        this.maxPrix = maxPrix;
        this.couleur = couleur;
        this.region = region;
        this.search = search;
    }

    @Override
    public Predicate toPredicate(Root<Vin> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // 1. ISOLATION MULTI-TENANT
        if (restaurantId != null) {
            predicates.add(criteriaBuilder.equal(root.get("restaurant").get("id"), restaurantId));
        } else {
            // Sécurité : Si on oublie l'ID, on ne renvoie rien.
            predicates.add(criteriaBuilder.disjunction());
        }

        // 2. LOGIQUE DE FILTRAGE
        if (minPrix != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("prix"), minPrix));
        }
        if (maxPrix != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("prix"), maxPrix));
        }
        if (couleur != null) {
            predicates.add(criteriaBuilder.equal(root.get("couleur"), couleur));
        }
        if (region != null && !region.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("region")), "%" + region.toLowerCase() + "%"));
        }
        if (search != null && !search.isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate nom = criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), searchPattern);
            Predicate cepage = criteriaBuilder.like(criteriaBuilder.lower(root.get("cepage")), searchPattern);
            Predicate notes = criteriaBuilder.like(criteriaBuilder.lower(root.get("notesDegustation")), searchPattern);
            predicates.add(criteriaBuilder.or(nom, cepage, notes));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}