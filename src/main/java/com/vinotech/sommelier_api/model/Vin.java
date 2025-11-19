package com.vinotech.sommelier_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// Remplacement de @Data par @Getter/@Setter
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vins")
public class Vin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // -> SERIAL/PRIMARY KEY

    @Column(nullable = false)
    private String nom;

    @Column(precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(nullable = false)
    private String region;

    @Column(name = "notes_degustation", columnDefinition = "TEXT")
    private String notesDegustation;

    @Enumerated(EnumType.STRING)
    private CouleurVin couleur;

    private String cepage;

    /**
     * Determine whether this Vin is equal to another object by comparing their persistent IDs.
     *
     * @param o the object to compare with this Vin
     * @return `true` if {@code o} is a {@code Vin} and both have the same non-null id, `false` otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vin)) return false;
        Vin vin = (Vin) o;
        // On se base uniquement sur l'ID de la DB
        return id != null && id.equals(vin.id);
    }

    /**
     * Compute a hash code based on the entity's runtime class.
     *
     * @return the hash code of the entity's runtime class
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // Relation Many-to-Many (Côté Possesseur)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "accord_vin_plat",
            joinColumns = @JoinColumn(name = "vin_id"),
            inverseJoinColumns = @JoinColumn(name = "plat_id")
    )
    private Set<Plat> platsAccordes = new HashSet<>();
}