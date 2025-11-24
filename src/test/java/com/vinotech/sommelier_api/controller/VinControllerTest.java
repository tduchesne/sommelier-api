package com.vinotech.sommelier_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.service.VinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VinController.class)
@DisplayName("VinController Unit Tests")
class VinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
                .couleur(CouleurVin.BULLE)
                .cepage("Pinot Noir")
                .build();
    }

    // ==================== POST /api/vins - Create Vin Tests ====================

    @Test
    @DisplayName("Should create a new vin with valid data")
    void shouldCreateVinWithValidData() throws Exception {
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

        when(vinService.save(any(Vin.class))).thenReturn(savedVin);

        // When & Then
        mockMvc.perform(post("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newVin)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.nom").value("Nouveau Vin"))
                .andExpect(jsonPath("$.prix").value(45.00))
                .andExpect(jsonPath("$.region").value("Loire"))
                .andExpect(jsonPath("$.notesDegustation").value("Fruité et léger"))
                .andExpect(jsonPath("$.couleur").value("ROSE"))
                .andExpect(jsonPath("$.cepage").value("Pinot Noir"));

        verify(vinService, times(1)).save(any(Vin.class));
    }

    @Test
    @DisplayName("Should create a vin with minimal required fields")
    void shouldCreateVinWithMinimalFields() throws Exception {
        // Given
        Vin minimalVin = Vin.builder()
                .nom("Vin Simple")
                .region("Alsace")
                .couleur(CouleurVin.BLANC)
                .build();

        Vin savedVin = Vin.builder()
                .id(5L)
                .nom("Vin Simple")
                .region("Alsace")
                .couleur(CouleurVin.BLANC)
                .build();

        when(vinService.save(any(Vin.class))).thenReturn(savedVin);

        // When & Then
        mockMvc.perform(post("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minimalVin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.nom").value("Vin Simple"))
                .andExpect(jsonPath("$.region").value("Alsace"))
                .andExpect(jsonPath("$.prix").doesNotExist())
                .andExpect(jsonPath("$.notesDegustation").doesNotExist())
                .andExpect(jsonPath("$.cepage").doesNotExist());

        verify(vinService, times(1)).save(any(Vin.class));
    }

    @Test
    @DisplayName("Should create a vin with all color types")
    void shouldCreateVinWithAllColorTypes() throws Exception {
        // Test each color type
        for (CouleurVin couleur : CouleurVin.values()) {
            Vin vin = Vin.builder()
                    .nom("Test " + couleur)
                    .region("Test Region")
                    .couleur(couleur)
                    .build();

            Vin savedVin = Vin.builder()
                    .id(1L)
                    .nom("Test " + couleur)
                    .region("Test Region")
                    .couleur(couleur)
                    .build();

            when(vinService.save(any(Vin.class))).thenReturn(savedVin);

            mockMvc.perform(post("/api/vins")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(vin)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.couleur").value(couleur.toString()));

            reset(vinService);
        }
    }

    @Test
    @DisplayName("Should create a vin with maximum length strings")
    void shouldCreateVinWithMaxLengthStrings() throws Exception {
        // Given - testing boundary conditions for string lengths
        String maxNom = "A".repeat(50);
        String maxRegion = "B".repeat(50);
        String maxCepage = "C".repeat(50);
        String longNotes = "D".repeat(1000); // TEXT field, can be very long

        Vin vin = Vin.builder()
                .nom(maxNom)
                .region(maxRegion)
                .cepage(maxCepage)
                .notesDegustation(longNotes)
                .couleur(CouleurVin.ROUGE)
                .build();

        Vin savedVin = Vin.builder()
                .id(6L)
                .nom(maxNom)
                .region(maxRegion)
                .cepage(maxCepage)
                .notesDegustation(longNotes)
                .couleur(CouleurVin.ROUGE)
                .build();

        when(vinService.save(any(Vin.class))).thenReturn(savedVin);

        // When & Then
        mockMvc.perform(post("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value(maxNom))
                .andExpect(jsonPath("$.region").value(maxRegion))
                .andExpect(jsonPath("$.cepage").value(maxCepage))
                .andExpect(jsonPath("$.notesDegustation").value(longNotes));
    }

    @Test
    @DisplayName("Should create a vin with zero price")
    void shouldCreateVinWithZeroPrice() throws Exception {
        // Given
        Vin vin = Vin.builder()
                .nom("Vin Gratuit")
                .region("Test")
                .prix(BigDecimal.ZERO)
                .couleur(CouleurVin.ROUGE)
                .build();

        Vin savedVin = Vin.builder()
                .id(7L)
                .nom("Vin Gratuit")
                .region("Test")
                .prix(BigDecimal.ZERO)
                .couleur(CouleurVin.ROUGE)
                .build();

        when(vinService.save(any(Vin.class))).thenReturn(savedVin);

        // When & Then
        mockMvc.perform(post("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prix").value(0.00));
    }

    @Test
    @DisplayName("Should create a vin with very high price")
    void shouldCreateVinWithVeryHighPrice() throws Exception {
        // Given - testing precision = 10, scale = 2
        Vin vin = Vin.builder()
                .nom("Vin Très Cher")
                .region("Test")
                .prix(new BigDecimal("99999999.99"))
                .couleur(CouleurVin.ROUGE)
                .build();

        Vin savedVin = Vin.builder()
                .id(8L)
                .nom("Vin Très Cher")
                .region("Test")
                .prix(new BigDecimal("99999999.99"))
                .couleur(CouleurVin.ROUGE)
                .build();

        when(vinService.save(any(Vin.class))).thenReturn(savedVin);

        // When & Then
        mockMvc.perform(post("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prix").value(99999999.99));
    }

    @Test
    @DisplayName("Should handle invalid JSON in POST request")
    void shouldHandleInvalidJsonInPostRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        verify(vinService, never()).save(any(Vin.class));
    }

    @Test
    @DisplayName("Should handle empty request body")
    void shouldHandleEmptyRequestBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        verify(vinService, never()).save(any(Vin.class));
    }

    // ==================== GET /api/vins - Get All Vins Tests ====================

    @Test
    @DisplayName("Should retrieve all vins when multiple exist")
    void shouldRetrieveAllVinsWhenMultipleExist() throws Exception {
        // Given
        List<Vin> vins = Arrays.asList(testVin1, testVin2, testVin3);
        when(vinService.findAll()).thenReturn(vins);

        // When & Then
        mockMvc.perform(get("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nom").value("Château Margaux"))
                .andExpect(jsonPath("$[0].prix").value(150.00))
                .andExpect(jsonPath("$[0].region").value("Bordeaux"))
                .andExpect(jsonPath("$[0].couleur").value("ROUGE"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nom").value("Chablis Grand Cru"))
                .andExpect(jsonPath("$[1].couleur").value("BLANC"))
                .andExpect(jsonPath("$[2].id").value(3L))
                .andExpect(jsonPath("$[2].nom").value("Champagne Brut"))
                .andExpect(jsonPath("$[2].couleur").value("BULLE"));

        verify(vinService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no vins exist")
    void shouldReturnEmptyListWhenNoVinsExist() throws Exception {
        // Given
        when(vinService.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$", empty()));

        verify(vinService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve single vin in list")
    void shouldRetrieveSingleVinInList() throws Exception {
        // Given
        when(vinService.findAll()).thenReturn(Collections.singletonList(testVin1));

        // When & Then
        mockMvc.perform(get("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nom").value("Château Margaux"));

        verify(vinService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle large list of vins")
    void shouldHandleLargeListOfVins() throws Exception {
        // Given - create a large list
        List<Vin> largeList = new java.util.ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            largeList.add(Vin.builder()
                    .id((long) i)
                    .nom("Vin " + i)
                    .region("Region " + i)
                    .couleur(CouleurVin.values()[i % CouleurVin.values().length])
                    .build());
        }
        when(vinService.findAll()).thenReturn(largeList);

        // When & Then
        mockMvc.perform(get("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(100)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[99].id").value(100));

        verify(vinService, times(1)).findAll();
    }

    // ==================== GET /api/vins/{id} - Get Vin By Id Tests ====================

    @Test
    @DisplayName("Should retrieve vin by id when it exists")
    void shouldRetrieveVinByIdWhenItExists() throws Exception {
        // Given
        when(vinService.findById(1L)).thenReturn(Optional.of(testVin1));

        // When & Then
        mockMvc.perform(get("/api/vins/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nom").value("Château Margaux"))
                .andExpect(jsonPath("$.prix").value(150.00))
                .andExpect(jsonPath("$.region").value("Bordeaux"))
                .andExpect(jsonPath("$.notesDegustation").value("Notes de fruits rouges et de chêne"))
                .andExpect(jsonPath("$.couleur").value("ROUGE"))
                .andExpect(jsonPath("$.cepage").value("Cabernet Sauvignon"));

        verify(vinService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return 404 when vin does not exist")
    void shouldReturn404WhenVinDoesNotExist() throws Exception {
        // Given
        when(vinService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/vins/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(vinService, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should return 404 for negative id")
    void shouldReturn404ForNegativeId() throws Exception {
        // Given
        when(vinService.findById(-1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/vins/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(vinService, times(1)).findById(-1L);
    }

    @Test
    @DisplayName("Should return 404 for id zero")
    void shouldReturn404ForIdZero() throws Exception {
        // Given
        when(vinService.findById(0L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/vins/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(vinService, times(1)).findById(0L);
    }

    @Test
    @DisplayName("Should handle very large id values")
    void shouldHandleVeryLargeIdValues() throws Exception {
        // Given
        Long largeId = Long.MAX_VALUE;
        when(vinService.findById(largeId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/vins/" + largeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(vinService, times(1)).findById(largeId);
    }

    @Test
    @DisplayName("Should handle invalid id format")
    void shouldHandleInvalidIdFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/vins/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(vinService, never()).findById(any());
    }

    @Test
    @DisplayName("Should retrieve different vins by their specific ids")
    void shouldRetrieveDifferentVinsByTheirSpecificIds() throws Exception {
        // Given
        when(vinService.findById(1L)).thenReturn(Optional.of(testVin1));
        when(vinService.findById(2L)).thenReturn(Optional.of(testVin2));
        when(vinService.findById(3L)).thenReturn(Optional.of(testVin3));

        // When & Then - Test each vin
        mockMvc.perform(get("/api/vins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Château Margaux"));

        mockMvc.perform(get("/api/vins/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Chablis Grand Cru"));

        mockMvc.perform(get("/api/vins/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Champagne Brut"));

        verify(vinService, times(1)).findById(1L);
        verify(vinService, times(1)).findById(2L);
        verify(vinService, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Should retrieve vin with null optional fields")
    void shouldRetrieveVinWithNullOptionalFields() throws Exception {
        // Given
        Vin vinWithNulls = Vin.builder()
                .id(10L)
                .nom("Vin Basique")
                .region("Test")
                .couleur(CouleurVin.ROUGE)
                .build();

        when(vinService.findById(10L)).thenReturn(Optional.of(vinWithNulls));

        // When & Then
        mockMvc.perform(get("/api/vins/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nom").value("Vin Basique"))
                .andExpect(jsonPath("$.region").value("Test"))
                .andExpect(jsonPath("$.prix").doesNotExist())
                .andExpect(jsonPath("$.notesDegustation").doesNotExist())
                .andExpect(jsonPath("$.cepage").doesNotExist());
    }

    // ==================== Edge Cases and Error Handling ====================

    @Test
    @DisplayName("Should handle service exception during create")
    void shouldHandleServiceExceptionDuringCreate() throws Exception {
        // Given
        when(vinService.save(any(Vin.class))).thenThrow(new RuntimeException("Database error"));

        Vin vin = Vin.builder()
                .nom("Test")
                .region("Test")
                .couleur(CouleurVin.ROUGE)
                .build();

        // When & Then
        mockMvc.perform(post("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vin)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Should handle service exception during findAll")
    void shouldHandleServiceExceptionDuringFindAll() throws Exception {
        // Given
        when(vinService.findAll()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/vins"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Should handle service exception during findById")
    void shouldHandleServiceExceptionDuringFindById() throws Exception {
        // Given
        when(vinService.findById(1L)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/vins/1"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Should handle missing Content-Type header")
    void shouldHandleMissingContentTypeHeader() throws Exception {
        // Given
        Vin vin = Vin.builder()
                .nom("Test")
                .region("Test")
                .couleur(CouleurVin.ROUGE)
                .build();

        // When & Then
        mockMvc.perform(post("/api/vins")
                        .content(objectMapper.writeValueAsString(vin)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Should handle wrong HTTP method on endpoints")
    void shouldHandleWrongHttpMethodOnEndpoints() throws Exception {
        // PUT on POST endpoint
        mockMvc.perform(put("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isMethodNotAllowed());

        // DELETE on GET endpoint
        mockMvc.perform(delete("/api/vins"))
                .andExpect(status().isMethodNotAllowed());

        // POST on GET by id endpoint
        mockMvc.perform(post("/api/vins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isMethodNotAllowed());
    }
}
