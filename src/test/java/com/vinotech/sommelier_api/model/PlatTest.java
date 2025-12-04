package com.vinotech.sommelier_api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Plat Model Unit Tests")
class PlatTest {

    private Plat plat;
    private Vin vin1;
    private Vin vin2;

    @BeforeEach
    void setUp() {
        plat = new Plat();
        plat.setId(1L);
        plat.setNom("Entrecôte Grillée");
        plat.setIngredients("Bœuf, sel, poivre, huile d'olive");
        plat.setAllergenes("Aucun");
        // Initialisation des nouveaux champs pour éviter les effets de bord
        plat.setAllergenesModifiables(null);
        plat.setOptionRemplacement(null);

        vin1 = Vin.builder()
                .id(1L)
                .nom("Bordeaux Rouge")
                .region("Bordeaux")
                .couleur(CouleurVin.ROUGE)
                .build();

        vin2 = Vin.builder()
                .id(2L)
                .nom("Bourgogne Rouge")
                .region("Bourgogne")
                .couleur(CouleurVin.ROUGE)
                .build();
    }

    // ==================== Constructor Tests ====================

    @Test
    @DisplayName("Should create plat with no-args constructor")
    void shouldCreatePlatWithNoArgsConstructor() {
        // When
        Plat newPlat = new Plat();

        // Then
        assertThat(newPlat).isNotNull();
        assertThat(newPlat.getId()).isNull();
        assertThat(newPlat.getNom()).isNull();
        assertThat(newPlat.getIngredients()).isNull();
        assertThat(newPlat.getAllergenes()).isNull();
        assertThat(newPlat.getAllergenesModifiables()).isNull(); // Nouveau check
        assertThat(newPlat.getOptionRemplacement()).isNull();     // Nouveau check
        assertThat(newPlat.getVinsAccordes()).isNotNull();
        assertThat(newPlat.getVinsAccordes()).isEmpty();
    }

    @Test
    @DisplayName("Should create plat with all-args constructor")
    void shouldCreatePlatWithAllArgsConstructor() {
        // Given
        Set<Vin> vins = new HashSet<>();
        vins.add(vin1);

        Set<MenuType> menuTypes = new HashSet<>(Arrays.asList(MenuType.LUNCH, MenuType.SOUPER));

        // When - Mise à jour de la signature du constructeur (Ajout des 2 nouveaux champs au milieu ou à la fin selon Lombok)
        // L'ordre Lombok suit généralement l'ordre de déclaration des champs dans la classe.
        // Ordre supposé: id, nom, ingredients, allergenes, allergenesModifiables, optionRemplacement, (vinsAccordes si présent)

        // Note : Si vinsAccordes n'est pas dans le constructeur (dépend de @ToString/@EqualsAndHashCode exclude), adapter ici.
        // On suppose ici que Lombok inclut tout.
        Plat newPlat = new Plat(
                1L,
                "Test Plat",
                "Ingredients",
                "Allergènes",
                "Gluten",           // allergenesModifiables
                "Enlever le pain",
                menuTypes,// optionRemplacement
                vins                // vinsAccordes
        );

        // Then
        assertThat(newPlat).isNotNull();
        assertThat(newPlat.getId()).isEqualTo(1L);
        assertThat(newPlat.getNom()).isEqualTo("Test Plat");
        assertThat(newPlat.getIngredients()).isEqualTo("Ingredients");
        assertThat(newPlat.getAllergenes()).isEqualTo("Allergènes");
        assertThat(newPlat.getAllergenesModifiables()).isEqualTo("Gluten");
        assertThat(newPlat.getOptionRemplacement()).isEqualTo("Enlever le pain");
        assertThat(newPlat.getVinsAccordes()).hasSize(1);
        assertThat(newPlat.getVinsAccordes()).contains(vin1);
    }

    @Test
    @DisplayName("Should initialize vinsAccordes as empty HashSet")
    void shouldInitializeVinsAccordesAsEmptyHashSet() {
        // When
        Plat newPlat = new Plat();

        // Then
        assertThat(newPlat.getVinsAccordes()).isNotNull();
        assertThat(newPlat.getVinsAccordes()).isEmpty();
        assertThat(newPlat.getVinsAccordes()).isInstanceOf(HashSet.class);
    }

    // ==================== Getters and Setters Tests ====================

    @Test
    @DisplayName("Should get and set id correctly")
    void shouldGetAndSetIdCorrectly() {
        Plat newPlat = new Plat();
        newPlat.setId(100L);
        assertThat(newPlat.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Should get and set nom correctly")
    void shouldGetAndSetNomCorrectly() {
        Plat newPlat = new Plat();
        newPlat.setNom("Salade César");
        assertThat(newPlat.getNom()).isEqualTo("Salade César");
    }

    @Test
    @DisplayName("Should get and set ingredients correctly")
    void shouldGetAndSetIngredientsCorrectly() {
        Plat newPlat = new Plat();
        newPlat.setIngredients("Laitue, poulet");
        assertThat(newPlat.getIngredients()).isEqualTo("Laitue, poulet");
    }

    @Test
    @DisplayName("Should get and set allergenes correctly")
    void shouldGetAndSetAllergenesCorrectly() {
        Plat newPlat = new Plat();
        newPlat.setAllergenes("Gluten");
        assertThat(newPlat.getAllergenes()).isEqualTo("Gluten");
    }

    // --- NOUVEAUX TESTS POUR SPRINT 7 ---

    @Test
    @DisplayName("Should get and set allergenesModifiables correctly")
    void shouldGetAndSetAllergenesModifiablesCorrectly() {
        // Given
        Plat newPlat = new Plat();
        String modifiable = "Gluten, Lait";

        // When
        newPlat.setAllergenesModifiables(modifiable);

        // Then
        assertThat(newPlat.getAllergenesModifiables()).isEqualTo(modifiable);
    }

    @Test
    @DisplayName("Should get and set optionRemplacement correctly")
    void shouldGetAndSetOptionRemplacementCorrectly() {
        // Given
        Plat newPlat = new Plat();
        String option = "Retirer les croûtons";

        // When
        newPlat.setOptionRemplacement(option);

        // Then
        assertThat(newPlat.getOptionRemplacement()).isEqualTo(option);
    }

    // ------------------------------------

    @Test
    @DisplayName("Should allow setting null values")
    void shouldAllowSettingNullValues() {
        plat.setNom(null);
        plat.setIngredients(null);
        plat.setAllergenes(null);
        plat.setAllergenesModifiables(null); // Updated
        plat.setOptionRemplacement(null);   // Updated

        assertThat(plat.getNom()).isNull();
        assertThat(plat.getIngredients()).isNull();
        assertThat(plat.getAllergenes()).isNull();
        assertThat(plat.getAllergenesModifiables()).isNull();
        assertThat(plat.getOptionRemplacement()).isNull();
    }

    // ==================== getVinsAccordes() Method Tests ====================
    // (Ces tests restent inchangés car ils ne dépendent pas du constructeur)

    @Test
    @DisplayName("Should get empty vinsAccordes initially")
    void shouldGetEmptyVinsAccordesInitially() {
        Set<Vin> vins = plat.getVinsAccordes();
        assertThat(vins).isNotNull();
        assertThat(vins).isEmpty();
    }

    @Test
    @DisplayName("Should get vinsAccordes with multiple vins")
    void shouldGetVinsAccordesWithMultipleVins() {
        vin1.addPlat(plat);
        vin2.addPlat(plat);
        Set<Vin> vins = plat.getVinsAccordes();
        assertThat(vins).hasSize(2);
        assertThat(vins).containsExactlyInAnyOrder(vin1, vin2);
    }

    // ==================== equals() & hashCode() Tests ====================

    @Test
    @DisplayName("Should return true when comparing plats with same id")
    void shouldReturnTrueWhenComparingPlatsWithSameId() {
        Plat plat2 = new Plat();
        plat2.setId(1L);
        plat2.setNom("Different Name");

        assertThat(plat.equals(plat2)).isTrue();
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Should create plat with all args constructor and null vins")
    void shouldCreatePlatWithAllArgsConstructorAndNullVins() {

        Set<MenuType> menuTypes = new HashSet<>(Arrays.asList(MenuType.LUNCH, MenuType.SOUPER));
        // Updated Constructor call
        Plat newPlat = new Plat(
                10L,
                "Test",
                "Ingredients",
                "Allergènes",
                null, // modifiable
                null, // option
                menuTypes,
                null // vins
        );

        assertThat(newPlat.getId()).isEqualTo(10L);
        assertThat(newPlat.getNom()).isEqualTo("Test");
        assertThat(newPlat.getAllergenesModifiables()).isNull();
        assertThat(newPlat.getVinsAccordes()).isNull();
    }

    @Test
    @DisplayName("Should get and set typesMenu correctly")
    void shouldGetAndSetTypesMenuCorrectly() {
        Plat newPlat = new Plat();
        newPlat.setTypesMenu(Collections.singleton(MenuType.BRUNCH));
        assertThat(newPlat.getTypesMenu()).contains(MenuType.BRUNCH);
    }
}