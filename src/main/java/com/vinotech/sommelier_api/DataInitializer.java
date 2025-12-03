package com.vinotech.sommelier_api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinotech.sommelier_api.model.Plat;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.PlatRepository;
import com.vinotech.sommelier_api.repository.VinRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.InputStream;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(VinRepository vinRepository, PlatRepository platRepository) {
        return args -> {
            loadDataIfEmpty(vinRepository, "vins.json", new TypeReference<List<Vin>>(){}, "vins");
            loadDataIfEmpty(platRepository, "plats.json", new TypeReference<List<Plat>>(){}, "plats");
        };
    }

    private <T> void loadDataIfEmpty(JpaRepository<T, Long> repository, String filename, TypeReference<List<T>> typeReference, String entityName) {
        if (repository.count() == 0) {
            System.out.println("📦 Base de " + entityName + " vide. Chargement...");
            try {
                ObjectMapper mapper = new ObjectMapper();
                InputStream inputStream = new ClassPathResource(filename).getInputStream();
                List<T> items = mapper.readValue(inputStream, typeReference);
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