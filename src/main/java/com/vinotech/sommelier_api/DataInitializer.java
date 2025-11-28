package com.vinotech.sommelier_api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.VinRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(VinRepository vinRepository) {
        return args -> {
            // On vérifie si la base est vide pour ne pas dupliquer les données
            if (vinRepository.count() == 0) {
                System.out.println("Base de données vide. Chargement des vins initiaux...");

                try {
                    // Lecture du fichier JSON
                    ObjectMapper mapper = new ObjectMapper();
                    InputStream inputStream = new ClassPathResource("vins.json").getInputStream();

                    // Conversion JSON -> Liste de Vins
                    List<Vin> vins = mapper.readValue(inputStream, new TypeReference<List<Vin>>(){});

                    // Sauvegarde en base
                    vinRepository.saveAll(vins);
                    System.out.println("✅ " + vins.size() + " vins ont été importés avec succès !");

                } catch (Exception e) {
                    System.out.println("❌ Erreur lors de l'import des vins : " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("La base de données contient déjà " + vinRepository.count() + " vins.");
            }
        };
    }
}