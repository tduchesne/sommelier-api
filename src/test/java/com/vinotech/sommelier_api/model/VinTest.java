package com.vinotech.sommelier_api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Vin Model Unit Tests")
class VinTest {

    private Vin vin;
    private Plat plat1;
    private Plat plat2;

    @BeforeEach
    void setUp() {
        vin = Vin.builder()
                .id(1L)
                .nom("Château Margaux")
                .prix(new BigDecimal("150.00"))
                .region("Bordeaux")
                .notesDegustation("Notes de fruits rouges et de chêne")
                .couleur(CouleurVin.ROUGE)
                .cepage("Cabernet Sauvignon")
                .build();

        plat1 = new Plat();
        plat1.setId(1L);
        plat1.setNom("Steak");

        plat2 = new Plat();
        plat2.setId(2L);
        plat2.setNom("Fromage");
    }

    // ==================== Builder Tests ====================

    @Test
    @DisplayName("Should build vin with all fields using builder")
    void shouldBuildVinWithAllFieldsUsingBuilder() {
        // When
        Vin result = Vin.builder()
                .id(1L)
                .nom("Test Vin")
                .prix(new BigDecimal("50.00"))
                .region("Loire")
                .notesDegustation("Test notes")
                .couleur(CouleurVin.BLANC)
                .cepage("Sauvignon Blanc")
                .build();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Test Vin");
        assertThat(result.getPrix()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(result.getRegion()).isEqualTo("Loire");
        assertThat(result.getNotesDegustation()).isEqualTo("Test notes");
        assertThat(result.getCouleur()).isEqualTo(CouleurVin.BLANC);
        assertThat(result.getCepage()).isEqualTo("Sauvignon Blanc");
    }

    @Test
    @DisplayName("Should build vin with minimal fields")
    void shouldBuildVinWithMinimalFields() {
        // When
        Vin result = Vin.builder()
                .nom("Minimal Vin")
                .region("Test")
                .couleur(CouleurVin.ROSE)
                .build();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Minimal Vin");
        assertThat(result.getRegion()).isEqualTo("Test");
        assertThat(result.getCouleur()).isEqualTo(CouleurVin.ROSE);
        assertThat(result.getId()).isNull();
        assertThat(result.getPrix()).isNull();
        assertThat(result.getNotesDegustation()).isNull();
        assertThat(result.getCepage()).isNull();
    }

    @Test
    @DisplayName("Should initialize platsAccordes as empty HashSet")
    void shouldInitializePlatsAccordesAsEmptyHashSet() {
        // When
        Vin newVin = Vin.builder().build();

        // Then
        assertThat(newVin.getPlatsAccordes()).isNotNull();
        assertThat(newVin.getPlatsAccordes()).isEmpty();
        assertThat(newVin.getPlatsAccordes()).isInstanceOf(HashSet.class);
    }

    // ==================== Getters and Setters Tests ====================

    @Test
    @DisplayName("Should get and set all fields correctly")
    void shouldGetAndSetAllFieldsCorrectly() {
        // Given
        Vin newVin = new Vin();

        // When
        newVin.setId(5L);
        newVin.setNom("New Wine");
        newVin.setPrix(new BigDecimal("75.50"));
        newVin.setRegion("Rhône");
        newVin.setNotesDegustation("Spicy notes");
        newVin.setCouleur(CouleurVin.ROUGE);
        newVin.setCepage("Syrah");

        // Then
        assertThat(newVin.getId()).isEqualTo(5L);
        assertThat(newVin.getNom()).isEqualTo("New Wine");
        assertThat(newVin.getPrix()).isEqualByComparingTo(new BigDecimal("75.50"));
        assertThat(newVin.getRegion()).isEqualTo("Rhône");
        assertThat(newVin.getNotesDegustation()).isEqualTo("Spicy notes");
        assertThat(newVin.getCouleur()).isEqualTo(CouleurVin.ROUGE);
        assertThat(newVin.getCepage()).isEqualTo("Syrah");
    }

    @Test
    @DisplayName("Should allow setting null values for optional fields")
    void shouldAllowSettingNullValuesForOptionalFields() {
        // When
        vin.setPrix(null);
        vin.setNotesDegustation(null);
        vin.setCepage(null);

        // Then
        assertThat(vin.getPrix()).isNull();
        assertThat(vin.getNotesDegustation()).isNull();
        assertThat(vin.getCepage()).isNull();
    }

    // ==================== addPlat() Method Tests ====================

    @Test
    @DisplayName("Should add plat successfully and maintain bidirectional relationship")
    void shouldAddPlatSuccessfullyAndMaintainBidirectionalRelationship() {
        // When
        vin.addPlat(plat1);

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(1);
        assertThat(vin.getPlatsAccordes()).contains(plat1);
        assertThat(plat1.getVinsAccordes()).contains(vin);
    }

    @Test
    @DisplayName("Should add multiple plats")
    void shouldAddMultiplePlats() {
        // When
        vin.addPlat(plat1);
        vin.addPlat(plat2);

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(2);
        assertThat(vin.getPlatsAccordes()).containsExactlyInAnyOrder(plat1, plat2);
        assertThat(plat1.getVinsAccordes()).contains(vin);
        assertThat(plat2.getVinsAccordes()).contains(vin);
    }

    @Test
    @DisplayName("Should not add duplicate plat")
    void shouldNotAddDuplicatePlat() {
        // When
        vin.addPlat(plat1);
        vin.addPlat(plat1); // Try to add same plat again

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(1);
        assertThat(vin.getPlatsAccordes()).contains(plat1);
    }

    @Test
    @DisplayName("Should handle null plat gracefully in addPlat")
    void shouldHandleNullPlatGracefullyInAddPlat() {
        // When
        vin.addPlat(null);

        // Then
        assertThat(vin.getPlatsAccordes()).isEmpty();
    }

    @Test
    @DisplayName("Should handle plat with null vinsAccordes")
    void shouldHandlePlatWithNullVinsAccordes() {
        // Given
        // Use the all-args constructor and pass null for vinsAccordes so the Plat truly has a null collection
        Plat platWithNullVins = new Plat(3L, "Special Plat", null, null, null);

        // When
        vin.addPlat(platWithNullVins);

        // Then
        assertThat(vin.getPlatsAccordes()).contains(platWithNullVins);
    }

    @Test
    @DisplayName("Should add plat when vin already has other plats")
    void shouldAddPlatWhenVinAlreadyHasOtherPlats() {
        // Given
        vin.addPlat(plat1);

        // When
        vin.addPlat(plat2);

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(2);
        assertThat(vin.getPlatsAccordes()).containsExactlyInAnyOrder(plat1, plat2);
    }

    @Test
    @DisplayName("Should maintain bidirectional sync when plat is added multiple times")
    void shouldMaintainBidirectionalSyncWhenPlatIsAddedMultipleTimes() {
        // When - add same plat multiple times
        vin.addPlat(plat1);
        vin.addPlat(plat1);
        vin.addPlat(plat1);

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(1);
        assertThat(plat1.getVinsAccordes()).hasSize(1);
    }

    // ==================== removePlat() Method Tests ====================

    @Test
    @DisplayName("Should remove plat successfully and maintain bidirectional relationship")
    void shouldRemovePlatSuccessfullyAndMaintainBidirectionalRelationship() {
        // Given
        vin.addPlat(plat1);

        // When
        vin.removePlat(plat1);

        // Then
        assertThat(vin.getPlatsAccordes()).isEmpty();
        assertThat(plat1.getVinsAccordes()).doesNotContain(vin);
    }

    @Test
    @DisplayName("Should remove one plat while keeping others")
    void shouldRemoveOnePlatWhileKeepingOthers() {
        // Given
        vin.addPlat(plat1);
        vin.addPlat(plat2);

        // When
        vin.removePlat(plat1);

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(1);
        assertThat(vin.getPlatsAccordes()).contains(plat2);
        assertThat(vin.getPlatsAccordes()).doesNotContain(plat1);
        assertThat(plat1.getVinsAccordes()).doesNotContain(vin);
        assertThat(plat2.getVinsAccordes()).contains(vin);
    }

    @Test
    @DisplayName("Should handle removing null plat gracefully")
    void shouldHandleRemovingNullPlatGracefully() {
        // Given
        vin.addPlat(plat1);

        // When
        vin.removePlat(null);

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(1);
        assertThat(vin.getPlatsAccordes()).contains(plat1);
    }

    @Test
    @DisplayName("Should handle removing non-existent plat")
    void shouldHandleRemovingNonExistentPlat() {
        // Given
        vin.addPlat(plat1);

        // When
        vin.removePlat(plat2); // plat2 was never added

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(1);
        assertThat(vin.getPlatsAccordes()).contains(plat1);
    }

    @Test
    @DisplayName("Should handle removing plat from empty set")
    void shouldHandleRemovingPlatFromEmptySet() {
        // When
        vin.removePlat(plat1);

        // Then
        assertThat(vin.getPlatsAccordes()).isEmpty();
    }

    @Test
    @DisplayName("Should handle removing same plat multiple times")
    void shouldHandleRemovingSamePlatMultipleTimes() {
        // Given
        vin.addPlat(plat1);

        // When
        vin.removePlat(plat1);
        vin.removePlat(plat1);
        vin.removePlat(plat1);

        // Then
        assertThat(vin.getPlatsAccordes()).isEmpty();
    }

    @Test
    @DisplayName("Should handle add and remove operations in sequence")
    void shouldHandleAddAndRemoveOperationsInSequence() {
        // When
        vin.addPlat(plat1);
        vin.addPlat(plat2);
        vin.removePlat(plat1);
        vin.addPlat(plat1);
        vin.removePlat(plat2);

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(1);
        assertThat(vin.getPlatsAccordes()).contains(plat1);
        assertThat(plat1.getVinsAccordes()).contains(vin);
        assertThat(plat2.getVinsAccordes()).doesNotContain(vin);
    }

    // ==================== equals() Method Tests ====================

    @Test
    @DisplayName("Should return true when comparing same instance")
    void shouldReturnTrueWhenComparingSameInstance() {
        // When & Then
        assertThat(vin.equals(vin)).isTrue();
    }

    @Test
    @DisplayName("Should return true when comparing vins with same id")
    void shouldReturnTrueWhenComparingVinsWithSameId() {
        // Given
        Vin vin2 = Vin.builder()
                .id(1L) // Same ID as vin
                .nom("Different Name")
                .region("Different Region")
                .couleur(CouleurVin.BLANC)
                .build();

        // When & Then
        assertThat(vin.equals(vin2)).isTrue();
        assertThat(vin2.equals(vin)).isTrue();
    }

    @Test
    @DisplayName("Should return false when comparing vins with different ids")
    void shouldReturnFalseWhenComparingVinsWithDifferentIds() {
        // Given
        Vin vin2 = Vin.builder()
                .id(2L)
                .nom("Château Margaux") // Same name
                .region("Bordeaux")
                .couleur(CouleurVin.ROUGE)
                .build();

        // When & Then
        assertThat(vin.equals(vin2)).isFalse();
        assertThat(vin2.equals(vin)).isFalse();
    }

    @Test
    @DisplayName("Should return false when comparing with null")
    void shouldReturnFalseWhenComparingWithNull() {
        // When & Then
        assertThat(vin.equals(null)).isFalse();
    }

    @Test
    @DisplayName("Should return false when comparing with different class")
    void shouldReturnFalseWhenComparingWithDifferentClass() {
        // Given
        String notAVin = "Not a Vin";

        // When & Then
        assertThat(vin.equals(notAVin)).isFalse();
    }

    @Test
    @DisplayName("Should return false when one vin has null id")
    void shouldReturnFalseWhenOneVinHasNullId() {
        // Given
        Vin vinWithNullId = Vin.builder()
                .nom("Test")
                .region("Test")
                .couleur(CouleurVin.ROUGE)
                .build(); // No ID set

        // When & Then
        assertThat(vin.equals(vinWithNullId)).isFalse();
        assertThat(vinWithNullId.equals(vin)).isFalse();
    }

    @Test
    @DisplayName("Should return false when both vins have null ids")
    void shouldReturnFalseWhenBothVinsHaveNullIds() {
        // Given
        Vin vin1 = Vin.builder().nom("Vin 1").region("Test").couleur(CouleurVin.ROUGE).build();
        Vin vin2 = Vin.builder().nom("Vin 2").region("Test").couleur(CouleurVin.BLANC).build();

        // When & Then
        assertThat(vin1.equals(vin2)).isFalse();
    }

    @Test
    @DisplayName("Should maintain equals contract with symmetry")
    void shouldMaintainEqualsContractWithSymmetry() {
        // Given
        Vin vin2 = Vin.builder()
                .id(1L)
                .nom("Different")
                .region("Different")
                .couleur(CouleurVin.BLANC)
                .build();

        // When & Then - symmetry: a.equals(b) == b.equals(a)
        assertThat(vin.equals(vin2)).isEqualTo(vin2.equals(vin));
    }

    @Test
    @DisplayName("Should maintain equals contract with transitivity")
    void shouldMaintainEqualsContractWithTransitivity() {
        // Given
        Vin vin2 = Vin.builder().id(1L).nom("A").region("Test").couleur(CouleurVin.ROUGE).build();
        Vin vin3 = Vin.builder().id(1L).nom("B").region("Test").couleur(CouleurVin.BLANC).build();

        // When & Then - transitivity: if a.equals(b) and b.equals(c), then a.equals(c)
        assertThat(vin.equals(vin2)).isTrue();
        assertThat(vin2.equals(vin3)).isTrue();
        assertThat(vin.equals(vin3)).isTrue();
    }

    // ==================== hashCode() Method Tests ====================

    @Test
    @DisplayName("Should return consistent hashCode for same object")
    void shouldReturnConsistentHashCodeForSameObject() {
        // When
        int hashCode1 = vin.hashCode();
        int hashCode2 = vin.hashCode();

        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    @DisplayName("Should return same hashCode for all Vin instances")
    void shouldReturnSameHashCodeForAllVinInstances() {
        // Given
        Vin vin2 = Vin.builder()
                .id(2L)
                .nom("Different Vin")
                .region("Different Region")
                .couleur(CouleurVin.BLANC)
                .build();

        // When & Then - hashCode is based on class, not id
        assertThat(vin.hashCode()).isEqualTo(vin2.hashCode());
        assertThat(vin.hashCode()).isEqualTo(Vin.class.hashCode());
    }

    @Test
    @DisplayName("Should return same hashCode for vins with same id")
    void shouldReturnSameHashCodeForVinsWithSameId() {
        // Given
        Vin vin2 = Vin.builder()
                .id(1L)
                .nom("Different Name")
                .region("Different Region")
                .couleur(CouleurVin.BLANC)
                .build();

        // When & Then
        assertThat(vin.hashCode()).isEqualTo(vin2.hashCode());
    }

    @Test
    @DisplayName("Should return same hashCode even with null id")
    void shouldReturnSameHashCodeEvenWithNullId() {
        // Given
        Vin vinWithNullId = Vin.builder()
                .nom("Test")
                .region("Test")
                .couleur(CouleurVin.ROUGE)
                .build();

        // When & Then
        assertThat(vin.hashCode()).isEqualTo(vinWithNullId.hashCode());
    }

    // ==================== Edge Cases and Boundary Tests ====================

    @Test
    @DisplayName("Should handle all CouleurVin enum values")
    void shouldHandleAllCouleurVinEnumValues() {
        // Test each color
        for (CouleurVin couleur : CouleurVin.values()) {
            Vin testVin = Vin.builder()
                    .nom("Test")
                    .region("Test")
                    .couleur(couleur)
                    .build();

            assertThat(testVin.getCouleur()).isEqualTo(couleur);
        }
    }

    @Test
    @DisplayName("Should handle very long notesDegustation")
    void shouldHandleVeryLongNotesDegustation() {
        // Given
        String veryLongNotes = "A".repeat(10000);

        // When
        vin.setNotesDegustation(veryLongNotes);

        // Then
        assertThat(vin.getNotesDegustation()).hasSize(10000);
        assertThat(vin.getNotesDegustation()).isEqualTo(veryLongNotes);
    }

    @Test
    @DisplayName("Should handle empty strings")
    void shouldHandleEmptyStrings() {
        // When
        vin.setNom("");
        vin.setRegion("");
        vin.setCepage("");
        vin.setNotesDegustation("");

        // Then
        assertThat(vin.getNom()).isEmpty();
        assertThat(vin.getRegion()).isEmpty();
        assertThat(vin.getCepage()).isEmpty();
        assertThat(vin.getNotesDegustation()).isEmpty();
    }

    @Test
    @DisplayName("Should handle BigDecimal precision and scale")
    void shouldHandleBigDecimalPrecisionAndScale() {
        // Given - precision = 10, scale = 2
        BigDecimal price1 = new BigDecimal("12345678.99");
        BigDecimal price2 = new BigDecimal("0.01");
        BigDecimal price3 = new BigDecimal("99999999.99");

        // When & Then
        vin.setPrix(price1);
        assertThat(vin.getPrix()).isEqualByComparingTo(price1);

        vin.setPrix(price2);
        assertThat(vin.getPrix()).isEqualByComparingTo(price2);

        vin.setPrix(price3);
        assertThat(vin.getPrix()).isEqualByComparingTo(price3);
    }

    @Test
    @DisplayName("Should use in HashSet correctly")
    void shouldUseInHashSetCorrectly() {
        // Given
        Set<Vin> vinSet = new HashSet<>();
        Vin vin2 = Vin.builder().id(1L).nom("Same ID").region("Test").couleur(CouleurVin.ROUGE).build();

        // When
        vinSet.add(vin);
        vinSet.add(vin2); // Same ID as vin

        // Then - Only one should be in set because equality is based on id
        assertThat(vinSet).hasSize(1);
    }

    @Test
    @DisplayName("Should handle complex plat operations")
    void shouldHandleComplexPlatOperations() {
        // Given
        Plat plat3 = new Plat();
        plat3.setId(3L);
        plat3.setNom("Dessert");

        // When - Complex sequence of operations
        vin.addPlat(plat1);
        vin.addPlat(plat2);
        vin.addPlat(plat3);
        vin.removePlat(plat2);
        vin.addPlat(plat2);
        vin.removePlat(plat1);
        vin.removePlat(plat3);

        // Then
        assertThat(vin.getPlatsAccordes()).hasSize(1);
        assertThat(vin.getPlatsAccordes()).contains(plat2);
    }
}