package com.vinotech.sommelier_api.repository;

import com.vinotech.sommelier_api.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // Spring Data génère automatiquement le SQL pour cette méthode grâce au nom
    Optional<Restaurant> findByCodeUnique(String codeUnique);
}