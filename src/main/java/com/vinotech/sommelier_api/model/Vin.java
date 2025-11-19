package com.vinotech.sommelier_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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
     * Ajoute un plat à l'accord et synchronise la relation bidirectionnelle.
     * @param plat Le plat à ajouter.
     */
    public void addPlat(Plat plat) {
        if (plat == null) {
            return;
        }
        this.platsAccordes.add(plat);
        plat.getVinsAccordes().add(this);
    }

    /**
     * Retire un plat de l'accord et synchronise la relation bidirectionnelle.
     * @param plat Le plat à retirer.
     */
    public void removePlat(Plat plat) {
        if (plat == null) {
            return;
        }
        this.platsAccordes.remove(plat);
        plat.getVinsAccordes().remove(this);
    }

    // Relation Many-to-Many (Côté Possesseur)
    @Setter(AccessLevel.NONE) // Empêche Lombok de générer setPlatsAccordes()
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