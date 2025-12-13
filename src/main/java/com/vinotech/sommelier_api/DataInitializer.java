package com.vinotech.sommelier_api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinotech.sommelier_api.model.Plat;
import com.vinotech.sommelier_api.model.Restaurant;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.PlatRepository;
import com.vinotech.sommelier_api.repository.RestaurantRepository;
import com.vinotech.sommelier_api.repository.VinRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(VinRepository vinRepository,
                                   PlatRepository platRepository,
                                   RestaurantRepository restaurantRepository) {
        return args -> {
            // 1. Charger les Restaurants depuis le JSON
            loadDataIfEmpty(
                    restaurantRepository,
                    "restaurants.json",
                    new TypeReference<List<Restaurant>>(){},
                    "restaurants",
                    null
            );

            // 2. Récupérer le restaurant "Par défaut" pour y lier les données
            // (On prend le premier de la liste pour l'instant)
            List<Restaurant> restos = restaurantRepository.findAll();
            if (restos.isEmpty()) {
                throw new RuntimeException("❌ ERREUR CRITIQUE : Aucun restaurant n'a été chargé depuis restaurants.json ! Impossible d'importer les vins.");
            }
            final Restaurant defaultResto = restos.get(0);
            System.out.println("🔗 Liaison des données au contexte : " + defaultResto.getNom());

            // 3. Charger les Vins (Liaison avec le Resto)
            loadDataIfEmpty(
                    vinRepository,
                    "vins.json",
                    new TypeReference<List<Vin>>(){},
                    "vins",
                    (vin) -> vin.setRestaurant(defaultResto)
            );

            // 4. Charger les Plats (Liaison avec le Resto)
            loadDataIfEmpty(
                    platRepository,
                    "plats.json",
                    new TypeReference<List<Plat>>(){},
                    "plats",
                    (plat) -> plat.setRestaurant(defaultResto)
            );
        };
    }

    /**
     * Méthode générique capable d'appliquer une logique (Consumer) sur chaque item avant sauvegarde
     */
    private <T> void loadDataIfEmpty(JpaRepository<T, Long> repository,
                                     String filename,
                                     TypeReference<List<T>> typeReference,
                                     String entityName,
                                     Consumer<T> postProcessor) {

        if (repository.count() == 0) {
            System.out.println("📦 Base de " + entityName + " vide. Chargement depuis JSON...");
            try {
                ObjectMapper mapper = new ObjectMapper();
                InputStream inputStream = new ClassPathResource(filename).getInputStream();
                List<T> items = mapper.readValue(inputStream, typeReference);

                // Application de la logique de liaison (ex: setRestaurant)
                if (postProcessor != null) {
                    for (T item : items) {
                        postProcessor.accept(item);
                    }
                }

                repository.saveAll(items);
                System.out.println("✅ " + items.size() + " " + entityName + " importés !");
            } catch (Exception e) {
                System.out.println("❌ Erreur import " + entityName + " : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("👌 La base contient déjà " + repository.count() + " " + entityName + ".");
        }
    }
}