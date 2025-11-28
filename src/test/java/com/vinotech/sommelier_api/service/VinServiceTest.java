package com.vinotech.sommelier_api.service;

import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.VinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VinService Unit Tests")
class VinServiceTest {

    @Mock
    private VinRepository vinRepository;

    @InjectMocks
    private VinService vinService;

    private Vin testVin1;
    private Vin testVin2;
    private Vin testVin3;

    @BeforeEach
    void setUp() {
        testVin1 = Vin.builder()
                .id(1L)
                .nom("Château Margaux")
                .prix(new BigDecimal("150.00"))
                .region("Bordeaux")
                .notesDegustation("Notes de fruits rouges et de chêne")
                .couleur(CouleurVin.ROUGE)
                .cepage("Cabernet Sauvignon")
                .build();

        testVin2 = Vin.builder()
                .id(2L)
                .nom("Chablis Grand Cru")
                .prix(new BigDecimal("85.50"))
                .region("Bourgogne")
                .notesDegustation("Minéral avec des notes de citron")
                .couleur(CouleurVin.BLANC)
                .cepage("Chardonnay")
                .build();

        testVin3 = Vin.builder()
                .id(3L)
                .nom("Champagne Brut")
                .prix(new BigDecimal("60.00"))
                .region("Champagne")
                .notesDegustation("Effervescent avec des notes de pomme")
                .couleur(CouleurVin.EFFERVESCENT)
                .cepage("Pinot Noir")
                .build();
    }

    // ==================== save() Method Tests ====================

    @Test
    @DisplayName("Should save a new vin successfully")
    void shouldSaveNewVinSuccessfully() {
        // Given
        Vin newVin = Vin.builder()
                .nom("Nouveau Vin")
                .prix(new BigDecimal("45.00"))
                .region("Loire")
                .notesDegustation("Fruité et léger")
                .couleur(CouleurVin.ROSE)
                .cepage("Pinot Noir")
                .build();

        Vin savedVin = Vin.builder()
                .id(4L)
                .nom("Nouveau Vin")
                .prix(new BigDecimal("45.00"))
                .region("Loire")
                .notesDegustation("Fruité et léger")
                .couleur(CouleurVin.ROSE)
                .cepage("Pinot Noir")
                .build();

        when(vinRepository.save(newVin)).thenReturn(savedVin);

        // When
        Vin result = vinService.save(newVin);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(4L);
        assertThat(result.getNom()).isEqualTo("Nouveau Vin");
        assertThat(result.getPrix()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getRegion()).isEqualTo("Loire");
        assertThat(result.getNotesDegustation()).isEqualTo("Fruité et léger");
        assertThat(result.getCouleur()).isEqualTo(CouleurVin.ROSE);
        assertThat(result.getCepage()).isEqualTo("Pinot Noir");

        verify(vinRepository, times(1)).save(newVin);
    }

    @Test
    @DisplayName("Should update an existing vin")
    void shouldUpdateExistingVin() {
        // Given
        Vin existingVin = Vin.builder()
                .id(1L)
                .nom("Château Margaux")
                .prix(new BigDecimal("150.00"))
                .region("Bordeaux")
                .couleur(CouleurVin.ROUGE)
                .build();

        Vin updatedVin = Vin.builder()
                .id(1L)
                .nom("Château Margaux")
                .prix(new BigDecimal("175.00")) // Updated price
                .region("Bordeaux")
                .couleur(CouleurVin.ROUGE)
                .build();

        when(vinRepository.save(existingVin)).thenReturn(updatedVin);

        // When
        Vin result = vinService.save(existingVin);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPrix()).isEqualByComparingTo(new BigDecimal("175.00"));

        verify(vinRepository, times(1)).save(existingVin);
    }

    @Test
    @DisplayName("Should save vin with minimal required fields")
    void shouldSaveVinWithMinimalRequiredFields() {
        // Given
        Vin minimalVin = Vin.builder()
                .nom("Vin Minimal")
                .region("Test")
                .couleur(CouleurVin.BLANC)
                .build();

        Vin savedVin = Vin.builder()
                .id(5L)
                .nom("Vin Minimal")
                .region("Test")
                .couleur(CouleurVin.BLANC)
                .build();

        when(vinRepository.save(minimalVin)).thenReturn(savedVin);

        // When
        Vin result = vinService.save(minimalVin);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getNom()).isEqualTo("Vin Minimal");
        assertThat(result.getRegion()).isEqualTo("Test");
        assertThat(result.getCouleur()).isEqualTo(CouleurVin.BLANC);
        assertThat(result.getPrix()).isNull();
        assertThat(result.getNotesDegustation()).isNull();
        assertThat(result.getCepage()).isNull();

        verify(vinRepository, times(1)).save(minimalVin);
    }

    @Test
    @DisplayName("Should save vin with all CouleurVin types")
    void shouldSaveVinWithAllCouleurVinTypes() {
        // Test each color
        for (CouleurVin couleur : CouleurVin.values()) {
            Vin vin = Vin.builder()
                    .nom("Test " + couleur)
                    .region("Test")
                    .couleur(couleur)
                    .build();

            Vin savedVin = Vin.builder()
                    .id(1L)
                    .nom("Test " + couleur)
                    .region("Test")
                    .couleur(couleur)
                    .build();

            when(vinRepository.save(any(Vin.class))).thenReturn(savedVin);

            // When
            Vin result = vinService.save(vin);

            // Then
            assertThat(result.getCouleur()).isEqualTo(couleur);

            reset(vinRepository);
        }
    }

    @Test
    @DisplayName("Should save vin with zero price")
    void shouldSaveVinWithZeroPrice() {
        // Given
        Vin vin = Vin.builder()
                .nom("Vin Gratuit")
                .region("Test")
                .prix(BigDecimal.ZERO)
                .couleur(CouleurVin.ROUGE)
                .build();

        Vin savedVin = Vin.builder()
                .id(6L)
                .nom("Vin Gratuit")
                .region("Test")
                .prix(BigDecimal.ZERO)
                .couleur(CouleurVin.ROUGE)
                .build();

        when(vinRepository.save(vin)).thenReturn(savedVin);

        // When
        Vin result = vinService.save(vin);

        // Then
        assertThat(result.getPrix()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should save vin with very high price")
    void shouldSaveVinWithVeryHighPrice() {
        // Given - precision = 10, scale = 2 allows up to 99999999.99
        BigDecimal highPrice = new BigDecimal("99999999.99");
        Vin vin = Vin.builder()
                .nom("Vin Très Cher")
                .region("Test")
                .prix(highPrice)
                .couleur(CouleurVin.ROUGE)
                .build();

        Vin savedVin = Vin.builder()
                .id(7L)
                .nom("Vin Très Cher")
                .region("Test")
                .prix(highPrice)
                .couleur(CouleurVin.ROUGE)
                .build();

        when(vinRepository.save(vin)).thenReturn(savedVin);

        // When
        Vin result = vinService.save(vin);

        // Then
        assertThat(result.getPrix()).isEqualByComparingTo(highPrice);
    }

    @Test
    @DisplayName("Should save vin with max length strings")
    void shouldSaveVinWithMaxLengthStrings() {
        // Given
        String maxNom = "A".repeat(100);
        String maxRegion = "B".repeat(100);
        String maxCepage = "C".repeat(100);
        String longNotes = "D".repeat(5000);

        Vin vin = Vin.builder()
                .nom(maxNom)
                .region(maxRegion)
                .cepage(maxCepage)
                .notesDegustation(longNotes)
                .couleur(CouleurVin.ROUGE)
                .build();

        Vin savedVin = Vin.builder()
                .id(8L)
                .nom(maxNom)
                .region(maxRegion)
                .cepage(maxCepage)
                .notesDegustation(longNotes)
                .couleur(CouleurVin.ROUGE)
                .build();

        when(vinRepository.save(vin)).thenReturn(savedVin);

        // When
        Vin result = vinService.save(vin);

        // Then
        assertThat(result.getNom()).hasSize(100);
        assertThat(result.getRegion()).hasSize(100);
        assertThat(result.getCepage()).hasSize(100);
        assertThat(result.getNotesDegustation()).hasSize(5000);
    }

    @Test
    @DisplayName("Should handle null vin gracefully")
    void shouldHandleNullVinGracefully() {
        // Given
        when(vinRepository.save(null)).thenThrow(new IllegalArgumentException("Vin cannot be null"));

        // When & Then
        assertThatThrownBy(() -> vinService.save(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vin cannot be null");

        verify(vinRepository, times(1)).save(null);
    }

    @Test
    @DisplayName("Should propagate repository exceptions")
    void shouldPropagateRepositoryExceptions() {
        // Given
        Vin vin = Vin.builder()
                .nom("Test")
                .region("Test")
                .couleur(CouleurVin.ROUGE)
                .build();

        when(vinRepository.save(vin)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> vinService.save(vin))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(vinRepository, times(1)).save(vin);
    }

    // ==================== findAll() Method Tests ====================

    @Test
    @DisplayName("Should retrieve all vins when multiple exist")
    void shouldRetrieveAllVinsWhenMultipleExist() {
        // Given
        List<Vin> vins = Arrays.asList(testVin1, testVin2, testVin3);
        when(vinRepository.findAll()).thenReturn(vins);

        // When
        List<Vin> result = vinService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(testVin1, testVin2, testVin3);
        assertThat(result.get(0).getNom()).isEqualTo("Château Margaux");
        assertThat(result.get(1).getNom()).isEqualTo("Chablis Grand Cru");
        assertThat(result.get(2).getNom()).isEqualTo("Champagne Brut");

        verify(vinRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no vins exist")
    void shouldReturnEmptyListWhenNoVinsExist() {
        // Given
        when(vinRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Vin> result = vinService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(vinRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return single vin in list")
    void shouldReturnSingleVinInList() {
        // Given
        when(vinRepository.findAll()).thenReturn(Collections.singletonList(testVin1));

        // When
        List<Vin> result = vinService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testVin1);

        verify(vinRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle large list of vins")
    void shouldHandleLargeListOfVins() {
        // Given
        List<Vin> largeList = new java.util.ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            largeList.add(Vin.builder()
                    .id((long) i)
                    .nom("Vin " + i)
                    .region("Region " + i)
                    .couleur(CouleurVin.values()[i % CouleurVin.values().length])
                    .build());
        }
        when(vinRepository.findAll()).thenReturn(largeList);

        // When
        List<Vin> result = vinService.findAll();

        // Then
        assertThat(result).hasSize(1000);
        assertThat(result.get(0).getNom()).isEqualTo("Vin 1");
        assertThat(result.get(999).getNom()).isEqualTo("Vin 1000");

        verify(vinRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should propagate findAll repository exceptions")
    void shouldPropagateFindAllRepositoryExceptions() {
        // Given
        when(vinRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> vinService.findAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(vinRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return list with vins of different colors")
    void shouldReturnListWithVinsOfDifferentColors() {
        // Given
        List<Vin> vins = Arrays.asList(
                Vin.builder().id(1L).nom("Rouge").region("Test").couleur(CouleurVin.ROUGE).build(),
                Vin.builder().id(2L).nom("Blanc").region("Test").couleur(CouleurVin.BLANC).build(),
                Vin.builder().id(3L).nom("Rosé").region("Test").couleur(CouleurVin.ROSE).build(),
                Vin.builder().id(4L).nom("Orange").region("Test").couleur(CouleurVin.ORANGE).build(),
                Vin.builder().id(5L).nom("Bulle").region("Test").couleur(CouleurVin.EFFERVESCENT).build()
        );
        when(vinRepository.findAll()).thenReturn(vins);

        // When
        List<Vin> result = vinService.findAll();

        // Then
        assertThat(result).hasSize(5);
        assertThat(result).extracting("couleur")
                .containsExactly(CouleurVin.ROUGE, CouleurVin.BLANC, CouleurVin.ROSE, 
                        CouleurVin.ORANGE, CouleurVin.EFFERVESCENT);
    }

    // ==================== findById() Method Tests ====================

    @Test
    @DisplayName("Should find vin by id when it exists")
    void shouldFindVinByIdWhenItExists() {
        // Given
        when(vinRepository.findById(1L)).thenReturn(Optional.of(testVin1));

        // When
        Optional<Vin> result = vinService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testVin1);
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getNom()).isEqualTo("Château Margaux");
        assertThat(result.get().getPrix()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(result.get().getRegion()).isEqualTo("Bordeaux");
        assertThat(result.get().getCouleur()).isEqualTo(CouleurVin.ROUGE);

        verify(vinRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty optional when vin does not exist")
    void shouldReturnEmptyOptionalWhenVinDoesNotExist() {
        // Given
        when(vinRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Vin> result = vinService.findById(999L);

        // Then
        assertThat(result).isEmpty();

        verify(vinRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should handle null id")
    void shouldHandleNullId() {
        // Given
        when(vinRepository.findById(null)).thenReturn(Optional.empty());

        // When
        Optional<Vin> result = vinService.findById(null);

        // Then
        assertThat(result).isEmpty();

        verify(vinRepository, times(1)).findById(null);
    }

    @Test
    @DisplayName("Should handle zero id")
    void shouldHandleZeroId() {
        // Given
        when(vinRepository.findById(0L)).thenReturn(Optional.empty());

        // When
        Optional<Vin> result = vinService.findById(0L);

        // Then
        assertThat(result).isEmpty();

        verify(vinRepository, times(1)).findById(0L);
    }

    @Test
    @DisplayName("Should handle negative id")
    void shouldHandleNegativeId() {
        // Given
        when(vinRepository.findById(-1L)).thenReturn(Optional.empty());

        // When
        Optional<Vin> result = vinService.findById(-1L);

        // Then
        assertThat(result).isEmpty();

        verify(vinRepository, times(1)).findById(-1L);
    }

    @Test
    @DisplayName("Should handle very large id")
    void shouldHandleVeryLargeId() {
        // Given
        Long largeId = Long.MAX_VALUE;
        when(vinRepository.findById(largeId)).thenReturn(Optional.empty());

        // When
        Optional<Vin> result = vinService.findById(largeId);

        // Then
        assertThat(result).isEmpty();

        verify(vinRepository, times(1)).findById(largeId);
    }

    @Test
    @DisplayName("Should find different vins by different ids")
    void shouldFindDifferentVinsByDifferentIds() {
        // Given
        when(vinRepository.findById(1L)).thenReturn(Optional.of(testVin1));
        when(vinRepository.findById(2L)).thenReturn(Optional.of(testVin2));
        when(vinRepository.findById(3L)).thenReturn(Optional.of(testVin3));

        // When
        Optional<Vin> result1 = vinService.findById(1L);
        Optional<Vin> result2 = vinService.findById(2L);
        Optional<Vin> result3 = vinService.findById(3L);

        // Then
        assertThat(result1).isPresent();
        assertThat(result1.get().getNom()).isEqualTo("Château Margaux");

        assertThat(result2).isPresent();
        assertThat(result2.get().getNom()).isEqualTo("Chablis Grand Cru");

        assertThat(result3).isPresent();
        assertThat(result3.get().getNom()).isEqualTo("Champagne Brut");

        verify(vinRepository, times(1)).findById(1L);
        verify(vinRepository, times(1)).findById(2L);
        verify(vinRepository, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Should propagate findById repository exceptions")
    void shouldPropagateFindByIdRepositoryExceptions() {
        // Given
        when(vinRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> vinService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(vinRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should find vin with minimal fields")
    void shouldFindVinWithMinimalFields() {
        // Given
        Vin minimalVin = Vin.builder()
                .id(10L)
                .nom("Minimal")
                .region("Test")
                .couleur(CouleurVin.BLANC)
                .build();

        when(vinRepository.findById(10L)).thenReturn(Optional.of(minimalVin));

        // When
        Optional<Vin> result = vinService.findById(10L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getPrix()).isNull();
        assertThat(result.get().getNotesDegustation()).isNull();
        assertThat(result.get().getCepage()).isNull();
    }

    @Test
    @DisplayName("Should call repository exactly once per findById call")
    void shouldCallRepositoryExactlyOncePerFindByIdCall() {
        // Given
        when(vinRepository.findById(1L)).thenReturn(Optional.of(testVin1));

        // When
        vinService.findById(1L);
        vinService.findById(1L);
        vinService.findById(1L);

        // Then
        verify(vinRepository, times(3)).findById(1L);
    }
}