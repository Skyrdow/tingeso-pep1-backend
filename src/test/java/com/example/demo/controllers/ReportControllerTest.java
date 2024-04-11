package com.example.demo.controllers;
import com.example.demo.entities.CarEntity;
import com.example.demo.entities.ReparationEntity;
import com.example.demo.entities.ReparationTypeEntity;
import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.enums.ReparationType;
import com.example.demo.services.CarService;
import com.example.demo.services.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.fail;
@WebMvcTest(ReportController.class)
public class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReportService reportService;
    @Test
    public void getReport1_Success() {
        given(reportService.getReport1()).willReturn(List.of(Map.of(
                "discounts", 1000,
                "car", "ABC123",
                "totalPrice", 14280,
                "surcharges", 0.0,
                "iva", 0.19,
                "basePrice", 11000)));
        try {
            mockMvc.perform(get("/api/v1/report/1"))
                    .andExpect(status().isOk()).andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$", hasSize(1)))
                   .andExpect(jsonPath("$[0].car", is("ABC123")))
                   .andExpect(jsonPath("$[0].discounts", is(1000)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void getReport2_Success() {
        given(reportService.getReport2()).willReturn(Map.of(
                CarType.Hatchback, Map.of(),
                CarType.Furgoneta, Map.of(),
                CarType.SUV, Map.of(
                    ReparationType.Frenos, Map.of("totalPrice", 1100000, "count", 5),
                        ReparationType.Combustible, Map.of("totalPrice",0, "count",1)
                ),
                CarType.Sedan, Map.of(),
                CarType.Pickup, Map.of()
        ));
        try {
            mockMvc.perform(get("/api/v1/report/2"))
                    .andExpect(status().isOk()).andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.report", hasSize(ReparationType.values().length)))
                   .andExpect(jsonPath("$.report[0].reparationType", is("Frenos")))
                   .andExpect(jsonPath("$.report[0].carTypes[2].carType", is("SUV")))
                   .andExpect(jsonPath("$.report[0].carTypes[2].totalPrice", is(1100000)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void getReport3_Success() {
        given(reportService.getReport3()).willReturn(Map.of(
                "Kia", 666000000,
                "Volvo", 816000000,
                "Honda", 746000000
    ));
        try {
            mockMvc.perform(get("/api/v1/report/3"))
                    .andExpect(status().isOk()).andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.Kia", is(666000000)))
                    .andExpect(jsonPath("$.Volvo", is(816000000)))
                    .andExpect(jsonPath("$.Honda", is(746000000)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void getReport4_Success() {
        given(reportService.getReport4()).willReturn(Map.of(
                MotorType.Diesel, Map.of(),
                MotorType.Electrico, Map.of(),
                MotorType.Gasolina, Map.of(
                    ReparationType.Frenos, Map.of("totalPrice", 1100000, "count", 5),
                        ReparationType.Combustible, Map.of("totalPrice",0, "count",1)
                ),
                MotorType.Hibrido, Map.of()
        ));
        try {
            mockMvc.perform(get("/api/v1/report/4"))
                    .andExpect(status().isOk()).andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.report", hasSize(ReparationType.values().length)))
                   .andExpect(jsonPath("$.report[0].reparationType", is("Frenos")))
                   .andExpect(jsonPath("$.report[0].motorTypes[0].motorType", is("Gasolina")))
                   .andExpect(jsonPath("$.report[0].motorTypes[0].totalPrice", is(1100000)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
