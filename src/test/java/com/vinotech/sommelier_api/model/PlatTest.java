package com.vinotech.sommelier_api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
        assertThat(newPlat.getVinsAccordes()).isNotNull();
        assertThat(newPlat.getVinsAccordes()).isEmpty();
    }

    @Test
    @DisplayName("Should create plat with all-args constructor")
    void shouldCreatePlatWithAllArgsConstructor() {
        // Given
        Set<Vin> vins = new HashSet<>();
        vins.add(vin1);

        // When
        Plat newPlat = new Plat(1L, "Test Plat", "Ingredients", "Allergènes", vins);

        // Then
        assertThat(newPlat).isNotNull();
        assertThat(newPlat.getId()).isEqualTo(1L);
        assertThat(newPlat.getNom()).isEqualTo("Test Plat");
        assertThat(newPlat.getIngredients()).isEqualTo("Ingredients");
        assertThat(newPlat.getAllergenes()).isEqualTo("Allergènes");
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
        // Given
        Plat newPlat = new Plat();

        // When
        newPlat.setId(100L);

        // Then
        assertThat(newPlat.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Should get and set nom correctly")
    void shouldGetAndSetNomCorrectly() {
        // Given
        Plat newPlat = new Plat();

        // When
        newPlat.setNom("Salade César");

        // Then
        assertThat(newPlat.getNom()).isEqualTo("Salade César");
    }

    @Test
    @DisplayName("Should get and set ingredients correctly")
    void shouldGetAndSetIngredientsCorrectly() {
        // Given
        Plat newPlat = new Plat();

        // When
        newPlat.setIngredients("Laitue, poulet, parmesan, croûtons, sauce césar");

        // Then
        assertThat(newPlat.getIngredients()).isEqualTo("Laitue, poulet, parmesan, croûtons, sauce césar");
    }

    @Test
    @DisplayName("Should get and set allergenes correctly")
    void shouldGetAndSetAllergenesCorrectly() {
        // Given
        Plat newPlat = new Plat();

        // When
        newPlat.setAllergenes("Gluten, lactose, œufs");

        // Then
        assertThat(newPlat.getAllergenes()).isEqualTo("Gluten, lactose, œufs");
    }

    @Test
    @DisplayName("Should allow setting null values")
    void shouldAllowSettingNullValues() {
        // When
        plat.setNom(null);
        plat.setIngredients(null);
        plat.setAllergenes(null);

        // Then
        assertThat(plat.getNom()).isNull();
        assertThat(plat.getIngredients()).isNull();
        assertThat(plat.getAllergenes()).isNull();
    }

    @Test
    @DisplayName("Should allow setting empty strings")
    void shouldAllowSettingEmptyStrings() {
        // When
        plat.setNom("");
        plat.setIngredients("");
        plat.setAllergenes("");

        // Then
        assertThat(plat.getNom()).isEmpty();
        assertThat(plat.getIngredients()).isEmpty();
        assertThat(plat.getAllergenes()).isEmpty();
    }

    // ==================== getVinsAccordes() Method Tests ====================

    @Test
    @DisplayName("Should get empty vinsAccordes initially")
    void shouldGetEmptyVinsAccordesInitially() {
        // When
        Set<Vin> vins = plat.getVinsAccordes();

        // Then
        assertThat(vins).isNotNull();
        assertThat(vins).isEmpty();
    }

    @Test
    @DisplayName("Should get vinsAccordes with multiple vins")
    void shouldGetVinsAccordesWithMultipleVins() {
        // Given
        vin1.addPlat(plat);
        vin2.addPlat(plat);

        // When
        Set<Vin> vins = plat.getVinsAccordes();

        // Then
        assertThat(vins).hasSize(2);
        assertThat(vins).containsExactlyInAnyOrder(vin1, vin2);
    }

    @Test
    @DisplayName("Should return reference to same set on multiple calls")
    void shouldReturnReferenceToSameSetOnMultipleCalls() {
        // When
        Set<Vin> vins1 = plat.getVinsAccordes();
        Set<Vin> vins2 = plat.getVinsAccordes();

        // Then
        assertThat(vins1).isSameAs(vins2);
    }

    // ==================== equals() Method Tests ====================

    @Test
    @DisplayName("Should return true when comparing same instance")
    void shouldReturnTrueWhenComparingSameInstance() {
        // When & Then
        assertThat(plat.equals(plat)).isTrue();
    }

    @Test
    @DisplayName("Should return true when comparing plats with same id")
    void shouldReturnTrueWhenComparingPlatsWithSameId() {
        // Given
        Plat plat2 = new Plat();
        plat2.setId(1L); // Same ID as plat
        plat2.setNom("Different Name");
        plat2.setIngredients("Different Ingredients");

        // When & Then
        assertThat(plat.equals(plat2)).isTrue();
        assertThat(plat2.equals(plat)).isTrue();
    }

    @Test
    @DisplayName("Should return false when comparing plats with different ids")
    void shouldReturnFalseWhenComparingPlatsWithDifferentIds() {
        // Given
        Plat plat2 = new Plat();
        plat2.setId(2L);
        plat2.setNom("Entrecôte Grillée"); // Same name
        plat2.setIngredients("Bœuf, sel, poivre, huile d'olive");

        // When & Then
        assertThat(plat.equals(plat2)).isFalse();
        assertThat(plat2.equals(plat)).isFalse();
    }

    @Test
    @DisplayName("Should return false when comparing with null")
    void shouldReturnFalseWhenComparingWithNull() {
        // When & Then
        assertThat(plat.equals(null)).isFalse();
    }

    @Test
    @DisplayName("Should return false when comparing with different class")
    void shouldReturnFalseWhenComparingWithDifferentClass() {
        // Given
        String notAPlat = "Not a Plat";

        // When & Then
        assertThat(plat.equals(notAPlat)).isFalse();
    }

    @Test
    @DisplayName("Should return false when one plat has null id")
    void shouldReturnFalseWhenOnePlatHasNullId() {
        // Given
        Plat platWithNullId = new Plat();
        platWithNullId.setNom("Test");

        // When & Then
        assertThat(plat.equals(platWithNullId)).isFalse();
        assertThat(platWithNullId.equals(plat)).isFalse();
    }

    @Test
    @DisplayName("Should return false when both plats have null ids")
    void shouldReturnFalseWhenBothPlatsHaveNullIds() {
        // Given
        Plat plat1 = new Plat();
        plat1.setNom("Plat 1");
        
        Plat plat2 = new Plat();
        plat2.setNom("Plat 2");

        // When & Then
        assertThat(plat1.equals(plat2)).isFalse();
    }

    @Test
    @DisplayName("Should maintain equals contract with symmetry")
    void shouldMaintainEqualsContractWithSymmetry() {
        // Given
        Plat plat2 = new Plat();
        plat2.setId(1L);
        plat2.setNom("Different");

        // When & Then - symmetry: a.equals(b) == b.equals(a)
        assertThat(plat.equals(plat2)).isEqualTo(plat2.equals(plat));
    }

    @Test
    @DisplayName("Should maintain equals contract with transitivity")
    void shouldMaintainEqualsContractWithTransitivity() {
        // Given
        Plat plat2 = new Plat();
        plat2.setId(1L);
        plat2.setNom("A");

        Plat plat3 = new Plat();
        plat3.setId(1L);
        plat3.setNom("B");

        // When & Then - transitivity: if a.equals(b) and b.equals(c), then a.equals(c)
        assertThat(plat.equals(plat2)).isTrue();
        assertThat(plat2.equals(plat3)).isTrue();
        assertThat(plat.equals(plat3)).isTrue();
    }

    // ==================== hashCode() Method Tests ====================

    @Test
    @DisplayName("Should return consistent hashCode for same object")
    void shouldReturnConsistentHashCodeForSameObject() {
        // When
        int hashCode1 = plat.hashCode();
        int hashCode2 = plat.hashCode();

        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    @DisplayName("Should return same hashCode for all Plat instances")
    void shouldReturnSameHashCodeForAllPlatInstances() {
        // Given
        Plat plat2 = new Plat();
        plat2.setId(2L);
        plat2.setNom("Different Plat");

        // When & Then - hashCode is based on class, not id
        assertThat(plat.hashCode()).isEqualTo(plat2.hashCode());
        assertThat(plat.hashCode()).isEqualTo(Plat.class.hashCode());
    }

    @Test
    @DisplayName("Should return same hashCode for plats with same id")
    void shouldReturnSameHashCodeForPlatsWithSameId() {
        // Given
        Plat plat2 = new Plat();
        plat2.setId(1L);
        plat2.setNom("Different Name");

        // When & Then
        assertThat(plat.hashCode()).isEqualTo(plat2.hashCode());
    }

    @Test
    @DisplayName("Should return same hashCode even with null id")
    void shouldReturnSameHashCodeEvenWithNullId() {
        // Given
        Plat platWithNullId = new Plat();
        platWithNullId.setNom("Test");

        // When & Then
        assertThat(plat.hashCode()).isEqualTo(platWithNullId.hashCode());
    }

    // ==================== Bidirectional Relationship Tests ====================

    @Test
    @DisplayName("Should maintain bidirectional relationship when vin adds plat")
    void shouldMaintainBidirectionalRelationshipWhenVinAddsPlat() {
        // When
        vin1.addPlat(plat);

        // Then
        assertThat(plat.getVinsAccordes()).contains(vin1);
        assertThat(vin1.getPlatsAccordes()).contains(plat);
    }

    @Test
    @DisplayName("Should maintain bidirectional relationship when vin removes plat")
    void shouldMaintainBidirectionalRelationshipWhenVinRemovesPlat() {
        // Given
        vin1.addPlat(plat);

        // When
        vin1.removePlat(plat);

        // Then
        assertThat(plat.getVinsAccordes()).doesNotContain(vin1);
        assertThat(vin1.getPlatsAccordes()).doesNotContain(plat);
    }

    @Test
    @DisplayName("Should handle multiple vins adding same plat")
    void shouldHandleMultipleVinsAddingSamePlat() {
        // When
        vin1.addPlat(plat);
        vin2.addPlat(plat);

        // Then
        assertThat(plat.getVinsAccordes()).hasSize(2);
        assertThat(plat.getVinsAccordes()).containsExactlyInAnyOrder(vin1, vin2);
    }

    @Test
    @DisplayName("Should handle one vin removing while others remain")
    void shouldHandleOneVinRemovingWhileOthersRemain() {
        // Given
        vin1.addPlat(plat);
        vin2.addPlat(plat);

        // When
        vin1.removePlat(plat);

        // Then
        assertThat(plat.getVinsAccordes()).hasSize(1);
        assertThat(plat.getVinsAccordes()).contains(vin2);
        assertThat(plat.getVinsAccordes()).doesNotContain(vin1);
    }

    // ==================== Edge Cases and Boundary Tests ====================

    @Test
    @DisplayName("Should handle very long nom")
    void shouldHandleVeryLongNom() {
        // Given - max length is 50
        String longNom = "A".repeat(50);

        // When
        plat.setNom(longNom);

        // Then
        assertThat(plat.getNom()).hasSize(50);
        assertThat(plat.getNom()).isEqualTo(longNom);
    }

    @Test
    @DisplayName("Should handle very long ingredients text")
    void shouldHandleVeryLongIngredientsText() {
        // Given - TEXT field can be very long
        String longIngredients = "Ingredient, ".repeat(1000);

        // When
        plat.setIngredients(longIngredients);

        // Then
        assertThat(plat.getIngredients()).isEqualTo(longIngredients);
    }

    @Test
    @DisplayName("Should handle very long allergenes text")
    void shouldHandleVeryLongAllergenesText() {
        // Given - TEXT field can be very long
        String longAllergenes = "Allergène, ".repeat(1000);

        // When
        plat.setAllergenes(longAllergenes);

        // Then
        assertThat(plat.getAllergenes()).isEqualTo(longAllergenes);
    }

    @Test
    @DisplayName("Should handle special characters in nom")
    void shouldHandleSpecialCharactersInNom() {
        // Given
        String specialNom = "Plat à l'été avec œufs & épices";

        // When
        plat.setNom(specialNom);

        // Then
        assertThat(plat.getNom()).isEqualTo(specialNom);
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void shouldHandleUnicodeCharacters() {
        // Given
        String unicodeNom = "Plat 日本料理 中国菜 한국 음식";

        // When
        plat.setNom(unicodeNom);

        // Then
        assertThat(plat.getNom()).isEqualTo(unicodeNom);
    }

    @Test
    @DisplayName("Should use in HashSet correctly")
    void shouldUseInHashSetCorrectly() {
        // Given
        Set<Plat> platSet = new HashSet<>();
        Plat plat2 = new Plat();
        plat2.setId(1L); // Same ID as plat
        plat2.setNom("Same ID");

        // When
        platSet.add(plat);
        platSet.add(plat2);

        // Then - Only one should be in set because equality is based on id
        assertThat(platSet).hasSize(1);
    }

    @Test
    @DisplayName("Should handle complex vin operations")
    void shouldHandleComplexVinOperations() {
        // Given
        Vin vin3 = Vin.builder().id(3L).nom("Vin 3").region("Test").couleur(CouleurVin.BLANC).build();

        // When - Complex sequence of operations
        vin1.addPlat(plat);
        vin2.addPlat(plat);
        vin3.addPlat(plat);
        vin2.removePlat(plat);
        vin2.addPlat(plat);
        vin1.removePlat(plat);
        vin3.removePlat(plat);

        // Then
        assertThat(plat.getVinsAccordes()).hasSize(1);
        assertThat(plat.getVinsAccordes()).contains(vin2);
    }

    @Test
    @DisplayName("Should handle null ingredients gracefully")
    void shouldHandleNullIngredientsGracefully() {
        // When
        plat.setIngredients(null);

        // Then
        assertThat(plat.getIngredients()).isNull();
    }

    @Test
    @DisplayName("Should handle null allergenes gracefully")
    void shouldHandleNullAllergenesGracefully() {
        // When
        plat.setAllergenes(null);

        // Then
        assertThat(plat.getAllergenes()).isNull();
    }

    @Test
    @DisplayName("Should create plat with all args constructor and null vins")
    void shouldCreatePlatWithAllArgsConstructorAndNullVins() {
        // When
        Plat newPlat = new Plat(10L, "Test", "Ingredients", "Allergènes", null);

        // Then
        assertThat(newPlat.getId()).isEqualTo(10L);
        assertThat(newPlat.getNom()).isEqualTo("Test");
        assertThat(newPlat.getVinsAccordes()).isNull();
    }
}