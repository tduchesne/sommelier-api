package com.vinotech.sommelier_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vins")
public class Vin {
    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // -> SERIAL/PRIMARY KEY

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(nullable = false, length = 50)
    private String region;

    @Column(name = "notes_degustation", columnDefinition = "TEXT")
    private String notesDegustation;

    @Enumerated(EnumType.STRING)
    private CouleurVin couleur;

    @Column(length = 50)
    private String cepage;

    /**
     * Associates the given Plat with this Vin and updates the bidirectional relationship.
     *
     * @param plat the Plat to associate with this Vin
     */
    public void addPlat(Plat plat) {
        if (plat == null) {
            return;
        }
        // Ensure the collection exists (defensive against builder/all-args constructor that may set it to null)
        if (this.platsAccordes == null) {
            this.platsAccordes = new HashSet<>();
        }
        if (this.platsAccordes.add(plat)) {
            Set<Vin> vinsAccordes = plat.getVinsAccordes();
            if (vinsAccordes != null) {
                vinsAccordes.add(this);
            }
        }
    }

    /**
     * Removes the given Plat from this Vin's accords and updates the bidirectional association.
     *
     * @param plat the Plat to remove from this Vin's accords
     */
    public void removePlat(Plat plat) {
        if (plat == null) {
            return;
        }
        // If the collection is null there's nothing to remove
        if (this.platsAccordes == null) {
            return;
        }
        if (this.platsAccordes.remove(plat)) {
            Set<Vin> vinsAccordes = plat.getVinsAccordes();
            if (vinsAccordes != null) {
                vinsAccordes.remove(this);
            }
        }
    }

    // Relation Many-to-Many (Côté Possesseur)
    @Setter(AccessLevel.NONE) // Empêche Lombok de générer setPlatsAccordes()
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "accord_vin_plat",
            joinColumns = @JoinColumn(name = "vin_id"),
            inverseJoinColumns = @JoinColumn(name = "plat_id")
    )
    private Set<Plat> platsAccordes = new HashSet<>();

    /**
     * Determine whether this Vin is equal to another object by comparing their persistent IDs.
     *
     * @param o the object to compare with this Vin
     * @return `true` if {@code o} is a {@code Vin} and both have the same non-null id, `false` otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vin vin = (Vin) o;
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
}