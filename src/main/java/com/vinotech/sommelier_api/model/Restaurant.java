package com.vinotech.sommelier_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Représente un établissement client (Restaurant).
 * C'est l'entité racine pour le Multi-Tenancy.
 */
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String adresse;

    // Code unique pour identifier le resto (utile plus tard pour les URLs ou API Keys)
    @Column(unique = true, nullable = false)
    private String codeUnique;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructeur vide requis par JPA
    public Restaurant() {}

    public Restaurant(String nom, String codeUnique) {
        this.nom = nom;
        this.codeUnique = codeUnique;
        this.createdAt = LocalDateTime.now();
    }

    // --- GETTERS & SETTERS ---
    // (JPA en a besoin pour lire/écrire les données)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getCodeUnique() { return codeUnique; }
    public void setCodeUnique(String codeUnique) { this.codeUnique = codeUnique; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}