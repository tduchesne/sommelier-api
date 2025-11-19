// src/main/java/com/vinotech/sommelier_api/model/Plat.java

package com.vinotech.sommelier_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "plats")
public class Plat {
    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(columnDefinition = "TEXT")
    private String allergenes;

    // Relation Many-to-Many (Côté Inverse)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "platsAccordes", fetch = FetchType.LAZY)
    private Set<Vin> vinsAccordes = new HashSet<>();

    public Set<Vin> getVinsAccordes() {
        return vinsAccordes;
    }

    /**
     * Determine whether this Plat is equal to another object based on its non-null id.
     *
     * @param o the object to compare with this Plat
     * @return `true` if the other object is a {@code Plat} with the same non-null id, `false` otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plat plat = (Plat) o;
        return id != null && id.equals(plat.id);
    }

    /**
     * Compute a hash code based on the entity's runtime class.
     *
     * @return the hash code of the runtime class, identical for all instances of the same class
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}