package com.vinotech.sommelier_api.service;

import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.VinRepository;
import com.vinotech.sommelier_api.repository.specs.VinSpecification;
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

    // ==================== searchVins() Method Tests (Remplace findAll) ====================

    @Test
    @DisplayName("Should search vins successfully with restaurant ID")
    void shouldSearchVinsSuccessfully() {
        // Given
        List<Vin> vins = Arrays.asList(testVin1, testVin2);
        // On mock findAll avec n'importe quelle Specification
        when(vinRepository.findAll(any(VinSpecification.class))).thenReturn(vins);

        // When
        // Test avec ID resto = 1 et aucun autre filtre
        List<Vin> result = vinService.searchVins(1L, null, null, null, null, null);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testVin1, testVin2);

        // Vérifie qu'on a bien appelé le repository avec une Specification
        verify(vinRepository, times(1)).findAll(any(VinSpecification.class));
    }

    @Test
    @DisplayName("Should return empty list when search yields no results")
    void shouldReturnEmptyListWhenSearchYieldsNoResults() {
        // Given
        when(vinRepository.findAll(any(VinSpecification.class))).thenReturn(Collections.emptyList());

        // When
        List<Vin> result = vinService.searchVins(1L, null, null, CouleurVin.ROUGE, "Inconnue", null);

        // Then
        assertThat(result).isEmpty();
        verify(vinRepository, times(1)).findAll(any(VinSpecification.class));
    }

    @Test
    @DisplayName("Should propagate repository exceptions during search")
    void shouldPropagateRepositoryExceptionsDuringSearch() {
        // Given
        when(vinRepository.findAll(any(VinSpecification.class))).thenThrow(new RuntimeException("DB Error"));

        // When & Then
        assertThatThrownBy(() -> vinService.searchVins(1L, null, null, null, null, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB Error");
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

    // ==================== save() Method Tests ====================
    // Note: Ces tests dépendent de l'existence de la méthode save() dans VinService.
    // Si tu ne l'as pas supprimée, garde-les. Sinon, supprime-les.
    // Je les laisse car ils semblent valides selon ton code précédent.

    @Test
    @DisplayName("Should save a new vin successfully")
    void shouldSaveNewVinSuccessfully() {
        // Given
        Vin newVin = Vin.builder().nom("New").build();
        Vin savedVin = Vin.builder().id(4L).nom("New").build();

        when(vinRepository.save(newVin)).thenReturn(savedVin);

        // When
        Vin result = vinService.save(newVin);

        // Then
        assertThat(result.getId()).isEqualTo(4L);
        verify(vinRepository, times(1)).save(newVin);
    }
}