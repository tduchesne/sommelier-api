package com.vinotech.sommelier_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Restaurant;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.service.RestaurantSecurityService;
import com.vinotech.sommelier_api.service.VinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VinController.class)
@DisplayName("VinController Unit Tests")
@ActiveProfiles("test")
@TestPropertySource(properties = "CLERK_ISSUER_URI=https://clerk.mock.test")
class VinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VinService vinService;

    // 1. AJOUT DU MOCK POUR LE SERVICE DE SÉCURITÉ
    @MockBean
    private RestaurantSecurityService securityService;

    private Vin testVin1;
    private Vin testVin2;
    private Vin testVin3;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        // Mock du Restaurant
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setNom("Que Sera Syrah");

        // 2. CONFIGURATION DU COMPORTEMENT DE SÉCURITÉ PAR DÉFAUT
        // Quand le contrôleur demandera l'ID au service, on renverra toujours 1L
        when(securityService.getRestaurantIdFromToken(any())).thenReturn(1L);

        testVin1 = Vin.builder()
                .id(1L)
                .nom("Château Margaux")
                .prix(new BigDecimal("150.00"))
                .region("Bordeaux")
                .notesDegustation("Notes de fruits rouges et de chêne")
                .couleur(CouleurVin.ROUGE)
                .cepage("Cabernet Sauvignon")
                .restaurant(restaurant)
                .build();

        testVin2 = Vin.builder()
                .id(2L)
                .nom("Chablis Grand Cru")
                .prix(new BigDecimal("85.50"))
                .region("Bourgogne")
                .notesDegustation("Minéral avec des notes de citron")
                .couleur(CouleurVin.BLANC)
                .cepage("Chardonnay")
                .restaurant(restaurant)
                .build();

        testVin3 = Vin.builder()
                .id(3L)
                .nom("Champagne Brut")
                .prix(new BigDecimal("60.00"))
                .region("Champagne")
                .notesDegustation("Effervescent avec des notes de pomme")
                .couleur(CouleurVin.EFFERVESCENT)
                .cepage("Pinot Noir")
                .restaurant(restaurant)
                .build();
    }

    // ==================== POST /api/vins - Create Vin Tests ====================

    @Test
    @DisplayName("Should create a new vin with valid data")
    void shouldCreateVinWithValidData() throws Exception {
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
                .restaurant(restaurant)
                .build();

        when(vinService.save(any(Vin.class))).thenReturn(savedVin);

        mockMvc.perform(post("/api/vins")
                        .with(jwt()) // <--- SIMULE UN TOKEN JWT VALIDE
                        .with(csrf()) // <--- NÉCESSAIRE POUR POST DANS LES TESTS
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newVin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.nom").value("Nouveau Vin"));

        verify(vinService, times(1)).save(any(Vin.class));
    }

    // ==================== GET /api/vins - Search Tests ====================

    @Test
    @DisplayName("Should retrieve all vins (search without filters) when multiple exist")
    void shouldRetrieveAllVinsWhenMultipleExist() throws Exception {
        // Given
        List<Vin> vins = Arrays.asList(testVin1, testVin2, testVin3);

        // On attend un appel avec l'ID 1L (venant du mock securityService)
        when(vinService.searchVins(eq(1L), any(), any(), any(), any(), any())).thenReturn(vins);

        // When & Then
        mockMvc.perform(get("/api/vins")
                        .with(jwt()) // <--- SIMULE UN TOKEN JWT
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].nom").value("Château Margaux"));

        verify(vinService, times(1)).searchVins(eq(1L), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should return empty list when no vins exist")
    void shouldReturnEmptyListWhenNoVinsExist() throws Exception {
        when(vinService.searchVins(eq(1L), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/vins")
                        .with(jwt()) // <--- SIMULE UN TOKEN JWT
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));

        verify(vinService, times(1)).searchVins(eq(1L), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should retrieve single vin in list")
    void shouldRetrieveSingleVinInList() throws Exception {
        when(vinService.searchVins(eq(1L), any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(testVin1));

        mockMvc.perform(get("/api/vins")
                        .with(jwt()) // <--- SIMULE UN TOKEN JWT
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(vinService, times(1)).searchVins(eq(1L), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should handle large list of vins")
    void shouldHandleLargeListOfVins() throws Exception {
        List<Vin> largeList = new java.util.ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            largeList.add(Vin.builder()
                    .id((long) i)
                    .nom("Vin " + i)
                    .restaurant(restaurant)
                    .build());
        }
        when(vinService.searchVins(eq(1L), any(), any(), any(), any(), any())).thenReturn(largeList);

        mockMvc.perform(get("/api/vins")
                        .with(jwt()) // <--- SIMULE UN TOKEN JWT
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(100)));
    }

    // ==================== GET /api/vins/{id} - Get Vin By Id Tests ====================

    @Test
    @DisplayName("Should retrieve vin by id when it exists")
    void shouldRetrieveVinByIdWhenItExists() throws Exception {
        when(vinService.findById(1L)).thenReturn(Optional.of(testVin1));

        mockMvc.perform(get("/api/vins/1")
                        .with(jwt()) // <--- SIMULE UN TOKEN JWT
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Château Margaux"));

        verify(vinService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return 404 when vin does not exist")
    void shouldReturn404WhenVinDoesNotExist() throws Exception {
        when(vinService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/vins/999")
                        .with(jwt()) // <--- SIMULE UN TOKEN JWT
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ==================== Edge Cases and Error Handling ====================

    @Test
    @DisplayName("Should handle service exception during search")
    void shouldHandleServiceExceptionDuringSearch() throws Exception {
        when(vinService.searchVins(eq(1L), any(), any(), any(), any(), any())).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/vins")
                        .with(jwt())) // <--- SIMULE UN TOKEN JWT
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Should handle wrong HTTP method")
    void shouldHandleWrongHttpMethod() throws Exception {
        mockMvc.perform(put("/api/vins")
                        .with(jwt()) // <--- SIMULE UN TOKEN JWT
                        .with(csrf()) // <--- CSRF pour les méthodes non-GET
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Should return 401 Unauthorized when no token provided")
    void shouldReturn401WhenNoTokenProvided() throws Exception {
        // Pas de .with(jwt()) ici
        mockMvc.perform(get("/api/vins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}