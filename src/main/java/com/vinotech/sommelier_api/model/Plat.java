// src/main/java/com/vinotech/sommelier_api/model/Plat.java

package com.vinotech.sommelier_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plats")
public class Plat {
    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(columnDefinition = "TEXT")
    private String allergenes;

    @Column(name = "allergenes_modifiables", columnDefinition = "TEXT")
    private String allergenesModifiables;

    @Column(name = "option_remplacement", columnDefinition = "TEXT")
    private String optionRemplacement;

    @ElementCollection(targetClass = MenuType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "plat_menu_types", joinColumns = @JoinColumn(name = "plat_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "menu_type")
    private Set<MenuType> typesMenu = new HashSet<>();

    /**
     * -- GETTER --
     *  Gets the wines associated with this plat.
     *
     * @return the {@code Set<Vin>} of wines associated with this plat; may be empty
     */
    // Relation Many-to-Many (Côté Inverse)
    @Getter
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "platsAccordes", fetch = FetchType.LAZY)
    private Set<Vin> vinsAccordes = new HashSet<>();

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