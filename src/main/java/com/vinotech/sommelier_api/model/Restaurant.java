package com.vinotech.sommelier_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Représente un établissement client (Restaurant).
 * C'est l'entité racine pour le Multi-Tenancy.
 */
@Setter
@Getter
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String adresse;

    // (Identifiant Métier / Humain)
    @Column(unique = true, nullable = false)
    private String codeUnique;

    // (Identifiant Technique / Auth)
    @Column(name = "clerk_id", unique = true, nullable = false)
    private String clerkId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructeur vide requis par JPA
    public Restaurant() {
    }

    public Restaurant(String nom, String codeUnique) {
        this.nom = nom;
        this.codeUnique = codeUnique;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        // Si la date n'est pas fixée, on met "Maintenant"
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}