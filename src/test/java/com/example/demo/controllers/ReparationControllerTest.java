package com.example.demo.controllers;
import com.example.demo.entities.CarEntity;
import com.example.demo.entities.ReparationEntity;
import com.example.demo.entities.ReparationTypeEntity;
import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.enums.ReparationType;
import com.example.demo.repositories.ReparationTypeRepository;
import com.example.demo.services.CarService;
import com.example.demo.services.ReparationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.fail;
@WebMvcTest(ReparationController.class)
public class ReparationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReparationService reparationService;

    @Test
    public void getReparations_Success() {
        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Frenos)), new Date(), new Date());
        ReparationEntity reparation2 = new ReparationEntity(1L, "ABCD23", new Date(), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());

        given(reparationService.getReparations()).willReturn(List.of(reparation1, reparation2));
        given(reparationService.getReparationTypes(reparation1)).willReturn(List.of(ReparationType.Combustible));
        given(reparationService.getReparationTypes(reparation1)).willReturn(List.of(ReparationType.Frenos));
        try {
            mockMvc.perform(get("/api/v1/reparation/"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.reparations", hasSize(2)))
                    .andExpect(jsonPath("$.reparations[0].patent", is("ABC123")))
                    .andExpect(jsonPath("$.reparations[1].patent", is("ABCD23")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void saveReparation_Success() {
        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(), new HashSet<>(), new Date(), new Date());
        reparation1.setReparationType(ReparationType.Frenos);
        String reparationJson = """
                {
                    "patent": "ABC123",
                    "admissionDate": "2024-03-31T10:00:00.000+00:00",
                    "reparationTypes": [{"reparationType": "Frenos"}],
                    "repairExitDate": "2024-04-07T18:00:00.000+00:00",
                    "retrievalDate": "2024-04-08T15:00:00.000+00:00"
                }
                """;

        try {
            given(reparationService.saveReparation(Mockito.any(ReparationEntity.class))).willReturn(reparation1);
            mockMvc.perform(post("/api/v1/reparation/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reparationJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.patent", is("ABC123")))
                    .andExpect(jsonPath("$.reparationTypes[0].reparationType", is("Frenos")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void saveReparation_Fail() {
        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(), new HashSet<>(), new Date(), new Date());
        String reparationJson = """
                {
                    "patent": "ABC123",
                    "admissionDate": "2024-03-31T10:00:00.000+00:00",
                    "reparationTypes": [],
                    "repairExitDate": "2024-04-07T18:00:00.000+00:00",
                    "retrievalDate": "2024-04-08T15:00:00.000+00:00"
                }
                """;

        try {
            given(reparationService.saveReparation(Mockito.any(ReparationEntity.class))).willThrow(new Exception("Reparation without type"));
            mockMvc.perform(post("/api/v1/reparation/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reparationJson));
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Reparation without type");
        }
    }
}
