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

    private static final long serialVersionUID = 1L;

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

    /**
     * Nettoie et échappe les caractères spéciaux pour les requêtes LIKE SQL.
     * Empêche l'injection de jokers (%) par l'utilisateur.
     */
    private String escapeLikePattern(String input) {
        if (input == null) return "";
        return input.trim().toLowerCase()
                .replace("\\", "\\\\") // Échapper l'antislash d'abord
                .replace("%", "\\%")   // Échapper le pourcent
                .replace("_", "\\_");  // Échapper l'underscore
    }

    @Override
    public Predicate toPredicate(Root<Vin> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // 1. ISOLATION
        if (restaurantId != null) {
            predicates.add(criteriaBuilder.equal(root.get("restaurant").get("id"), restaurantId));
        } else {
            predicates.add(criteriaBuilder.disjunction());
        }

        // 2. FILTRES
        if (minPrix != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("prix"), minPrix));
        }

        if (maxPrix != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("prix"), maxPrix));
        }

        if (couleur != null) {
            predicates.add(criteriaBuilder.equal(root.get("couleur"), couleur));
        }

        // Utilisation de l'échappement ('\') pour sécuriser le LIKE
        if (region != null && !region.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("region")),
                    "%" + escapeLikePattern(region) + "%",
                    '\\' // Caractère d'échappement
            ));
        }

        if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + escapeLikePattern(search) + "%";

            Predicate nomPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), searchPattern, '\\');
            Predicate cepagePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("cepage")), searchPattern, '\\');
            Predicate notesPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("notesDegustation")), searchPattern, '\\');

            predicates.add(criteriaBuilder.or(nomPredicate, cepagePredicate, notesPredicate));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}   