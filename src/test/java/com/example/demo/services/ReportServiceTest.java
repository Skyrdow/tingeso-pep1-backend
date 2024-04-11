package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.ReparationEntity;
import com.example.demo.entities.ReparationTypeEntity;
import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.enums.ReparationType;
import com.example.demo.repositories.ReparationRepository;
import com.example.demo.repositories.ReparationTypeRepository;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class ReportServiceTest {
    @Mock
    ReparationRepository reparationRepository;
    @Mock
    CarService carService;
    @Mock
    ReparationTypeRepository reparationTypeRepository;
    @Mock
    ReparationService reparationService;
    @InjectMocks
    ReportService reportService;

    @Test
    public void whenCalculateReparationPrice_Successful() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 5000L, 0L);

        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());

        when(reparationRepository.findByPatent(car1.getPatent())).thenReturn(List.of(reparation1));
        when(reparationService.getReparationTypes(reparation1)).thenReturn(List.of(ReparationType.Combustible));
        when(reparationService.reparationOnLast12MonthsCount(car1)).thenReturn(1);
        // When
        Float calculatedPrice = null;
        try {
            calculatedPrice = reportService.calculateReparationPrice(car1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        // Then
        assertThat(calculatedPrice).isEqualTo(154938);
    }
    @Test
    public void whenGetReparationPrices_Successful() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 5000L, 0L);
        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());

        when(reparationService.getReparationTypes(reparation1)).thenReturn(List.of(ReparationType.Combustible));
        assertThat(reportService.getReparationPrices(List.of(reparation1), car1)).isEqualTo(140000L);
    }
    @Test
    public void whenGetSurcharges_NoSurcharge() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 5000L, 0L);
        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());

        try {
            assertThat(reportService.getSurcharges(List.of(reparation1), car1, 140000L)).isEqualTo(0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void whenGetSurcharges_EverySurcharge() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(1000), MotorType.Diesel, 5, 1005000L, 0L);
        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(100000), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(200000), new Date(3500000));

        try {
            assertThat(reportService.getSurcharges(List.of(reparation1), car1, 140000L)).isCloseTo(49000, Percentage.withPercentage(10));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void whenGetDiscounts_NoDiscount() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1005000L, 0L);
        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date(100000));
        when(reparationService.reparationOnLast12MonthsCount(car1)).thenReturn(0);
        try {
            assertThat(reportService.getDiscounts(List.of(reparation1), car1, 140000L)).isEqualTo(0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void whenGetDiscounts_EveryDiscount() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1005000L, 1000L);
        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());
        ReparationEntity reparation2 = new ReparationEntity(2L, "ABC123", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());
        ReparationEntity reparation3 = new ReparationEntity(3L, "ABC123", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());


        when(reparationService.reparationOnLast12MonthsCount(car1)).thenReturn(3);
        try {
            assertThat(reportService.getDiscounts(List.of(reparation1, reparation2, reparation3), car1, 140000L)).isCloseTo(17800, Percentage.withPercentage(10));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void whenApplyIva_Success() {
        assertThat(reportService.applyIva(100f)).isCloseTo(119, Percentage.withPercentage(1));
    }

    @Test
    public void whenGetReport1() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 5000L, 0L);
        CarEntity car2 = new CarEntity("ABC345", "Volvo", "Model A", CarType.SUV, new Date(), MotorType.Electrico, 5, 5000L, 0L);

        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());
        ReparationEntity reparation2 = new ReparationEntity(1L, "ABC345", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Frenos)), new Date(), new Date());


        when(carService.getCars()).thenReturn(List.of(car1, car2));
        when(reparationRepository.findByPatent(car1.getPatent())).thenReturn(List.of(reparation1));
        when(reparationRepository.findByPatent(car2.getPatent())).thenReturn(List.of(reparation2));
        when(reparationService.getReparationTypes(reparation1)).thenReturn(List.of(ReparationType.Combustible));
        when(reparationService.getReparationTypes(reparation2)).thenReturn(List.of(ReparationType.Frenos));

        List<Map> report1 = reportService.getReport1();
        System.out.println(report1);
        assertThat(report1.size()).isEqualTo(2);
        assertThat(report1.get(0).get("car")).isEqualTo("ABC123");
        assertThat(report1.get(0).get("totalPrice")).isEqualTo(166600f);
    }
    @Test
    public void whenGetReport2() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 5000L, 0L);
        CarEntity car2 = new CarEntity("ABC345", "Volvo", "Model A", CarType.Sedan, new Date(), MotorType.Electrico, 5, 5000L, 0L);

        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());
        ReparationEntity reparation2 = new ReparationEntity(1L, "ABC345", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Frenos)), new Date(), new Date());
        ReparationEntity reparation3 = new ReparationEntity(1L, "ABC345", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());


        when(carService.getCarsByCarType(CarType.Sedan)).thenReturn(List.of(car1, car2));
        when(reparationService.getReparationsByPatent(car1.getPatent())).thenReturn(List.of(reparation1));
        when(reparationService.getReparationsByPatent(car2.getPatent())).thenReturn(List.of(reparation2, reparation3));
        when(reparationService.getReparationTypes(reparation1)).thenReturn(List.of(ReparationType.Combustible));
        when(reparationService.getReparationTypes(reparation2)).thenReturn(List.of(ReparationType.Frenos, ReparationType.Combustible));

        Map<CarType, Map> report2 = reportService.getReport2();
        System.out.println(report2);
        assertThat(report2.size()).isEqualTo(CarType.values().length);
        Map frenosMap = (Map) report2.get(CarType.Sedan).get(ReparationType.Frenos);
        Map combustibleMap = (Map) report2.get(CarType.Sedan).get(ReparationType.Combustible);
        assertThat(frenosMap.get("count")).isEqualTo(1);
        assertThat(combustibleMap.get("count")).isEqualTo(2);
        assertThat(combustibleMap.get("totalPrice")).isEqualTo(140000L);
    }
    @Test
    public void whenGetReport3() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 5000L, 0L);
        CarEntity car2 = new CarEntity("ABC345", "Volvo", "Model A", CarType.Sedan, new Date(), MotorType.Electrico, 5, 5000L, 0L);

        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());
        ReparationEntity reparation2 = new ReparationEntity(1L, "ABC345", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Frenos)), new Date(), new Date());
        ReparationEntity reparation3 = new ReparationEntity(1L, "ABC345", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());

        when(carService.getBrands()).thenReturn(List.of("Kia", "Volvo"));
        when(carService.getCarsByBrand("Kia")).thenReturn(List.of(car1));
        when(carService.getCarsByBrand("Volvo")).thenReturn(List.of(car2));
        when(reparationRepository.findByPatent(car1.getPatent())).thenReturn(List.of(reparation1));
        when(reparationRepository.findByPatent(car2.getPatent())).thenReturn(List.of(reparation2, reparation3));
        when(reparationService.getReparationTime(reparation1)).thenReturn(1000L);
        when(reparationService.getReparationTime(reparation2)).thenReturn(1000L);
        when(reparationService.getReparationTime(reparation3)).thenReturn(2000L);
        Map report3 = reportService.getReport3();
        System.out.println(report3);
        assertThat(report3.get("Kia")).isEqualTo(1000L);
        assertThat(report3.get("Volvo")).isEqualTo(1500L);
    }
    @Test
    public void whenGetReport4() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 5000L, 0L);
        CarEntity car2 = new CarEntity("ABC345", "Volvo", "Model A", CarType.Sedan, new Date(), MotorType.Diesel, 5, 5000L, 0L);

        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());
        ReparationEntity reparation2 = new ReparationEntity(1L, "ABC345", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Frenos)), new Date(), new Date());
        ReparationEntity reparation3 = new ReparationEntity(1L, "ABC345", new Date(1680835581994L), Set.of(new ReparationTypeEntity(1L, null, 1L, ReparationType.Combustible)), new Date(), new Date());


        when(carService.getCarsByMotorType(MotorType.Diesel)).thenReturn(List.of(car1, car2));
        when(carService.getCarsByMotorType(MotorType.Gasolina)).thenReturn(new ArrayList<>());
        when(reparationService.getReparationsByPatent(car1.getPatent())).thenReturn(List.of(reparation1));
        when(reparationService.getReparationsByPatent(car2.getPatent())).thenReturn(List.of(reparation2, reparation3));
        when(reparationService.getReparationTypes(reparation1)).thenReturn(List.of(ReparationType.Combustible));
        when(reparationService.getReparationTypes(reparation2)).thenReturn(List.of(ReparationType.Frenos, ReparationType.Combustible));

        Map<MotorType, Map> report4 = reportService.getReport4();

        System.out.println(report4);
        assertThat(report4.size()).isEqualTo(MotorType.values().length);
        Map combustibleDieselMap = (Map) report4.get(MotorType.Diesel).get(ReparationType.Combustible);
        Map frenosDieselMap = (Map) report4.get(MotorType.Diesel).get(ReparationType.Frenos);
        Map electricoMap = (Map) report4.get(MotorType.Electrico);
        assertThat(electricoMap).isEqualTo(Map.of());
        assertThat(combustibleDieselMap.get("count")).isEqualTo(2);
        assertThat(combustibleDieselMap.get("totalPrice")).isEqualTo(280000L);
        assertThat(frenosDieselMap.get("count")).isEqualTo(1);
        assertThat(frenosDieselMap.get("totalPrice")).isEqualTo(120000L);
    }
}
