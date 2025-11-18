package com.vinotech.sommelier_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vins")
public class Vin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // -> SERIAL/PRIMARY KEY

    @Column(nullable = false)
    private String nom;

    private Double prix;

    @Column(nullable = false)
    private String region;

    @Column(columnDefinition = "TEXT")
    private String notes_degustation;

    private String cepage;

    private String couleur;
}