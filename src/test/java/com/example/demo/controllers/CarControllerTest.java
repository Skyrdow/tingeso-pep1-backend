package com.example.demo.controllers;

import com.example.demo.entities.CarEntity;
import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.services.CarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.fail;


@WebMvcTest(CarController.class)
public class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarService carService;
    @Test
    public void listCars_Success() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1005000L, 0L);
        CarEntity car2 = new CarEntity("ABCD23", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1005000L, 0L);
        given(carService.getCars()).willReturn(List.of(car1, car2));
        try {
            mockMvc.perform(get("/api/v1/cars/"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].patent", is("ABC123")))
                    .andExpect(jsonPath("$[1].patent", is("ABCD23")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void saveCar_Success() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1005000L, 0L);
        given(carService.saveCar(Mockito.any(CarEntity.class))).willReturn(car1);
        String carJson = """
        {
            "patent": "ABC123",
            "brand": "Toyota",
            "model": "Corolla",
            "carType": "Sedan",
            "fabDate": "2023-01-01T00:00:00.000+00:00",
            "motorType": "Gasolina",
            "seatCount": 5,
            "mileage": 5000
        }
        """;

        try {
            mockMvc.perform(post("/api/v1/cars/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(carJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.patent", is("ABC123")))
                    .andExpect(jsonPath("$.brand", is("Kia")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void addBrandBonus_Success() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1005000L, 1000L);
        given(carService.setBrandBonus("ABC123", 1000L)).willReturn(car1);

        try {
            mockMvc.perform(post("/api/v1/cars/brandBonus/ABC123/1000")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.patent", is("ABC123")))
                    .andExpect(jsonPath("$.brand", is("Kia")))
                    .andExpect(jsonPath("$.brandBonus", is(1000)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
