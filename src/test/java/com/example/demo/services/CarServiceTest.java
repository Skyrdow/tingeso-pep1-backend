package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.repositories.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock
    CarRepository carRepository;
    @InjectMocks
    CarService carService;

    @Test
    public void whenGetCars_thenSuccess() {
        List<CarEntity> cars = new ArrayList<>();
        cars.add(new CarEntity("ABC123", "Kia", "Modelo S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1000L, 0L));
        cars.add(new CarEntity("BCD123", "Kia", "Modelo S", CarType.SUV, new Date(), MotorType.Electrico, 5, 1000L, 0L));

        when(carRepository.findAll()).thenReturn(cars);

        assertThat(carService.getCars()).isEqualTo(cars);
    }
    @Test
    public void whenSaveCar_thenSuccess() {
        CarEntity car = new CarEntity("ABC123", "Kia", "Modelo S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1000L, 0L);

        when(carRepository.save(car)).thenReturn(car);

        assertThat(carService.saveCar(car)).isEqualTo(car);
    }
    @Test
    public void whenGetCarsByCarType_thenSuccess() {
        List<CarEntity> cars = new ArrayList<>();
        cars.add(new CarEntity("ABC123", "Kia", "Modelo S", CarType.Hatchback, new Date(), MotorType.Diesel, 5, 1000L, 0L));
        cars.add(new CarEntity("BCD123", "Kia", "Modelo S", CarType.Hatchback, new Date(), MotorType.Electrico, 5, 1000L, 0L));

        when(carRepository.findByCarType(CarType.Hatchback)).thenReturn(cars);

        assertThat(carService.getCarsByCarType(CarType.Hatchback)).isEqualTo(cars);
    }
    @Test
    public void whenGetCarsByMotorType_thenSuccess() {
        List<CarEntity> cars = new ArrayList<>();
        cars.add(new CarEntity("ABC123", "Kia", "Modelo S", CarType.Hatchback, new Date(), MotorType.Diesel, 5, 1000L, 0L));
        cars.add(new CarEntity("BCD123", "Kia", "Modelo S", CarType.Hatchback, new Date(), MotorType.Diesel, 5, 1000L, 0L));

        when(carRepository.findByMotorType(MotorType.Diesel)).thenReturn(cars);

        assertThat(carService.getCarsByMotorType(MotorType.Diesel)).isEqualTo(cars);
    }
    @Test
    public void whenGetCarsByBrand_thenSuccess() {
        List<CarEntity> cars = new ArrayList<>();
        cars.add(new CarEntity("ABC123", "Kia", "Modelo S", CarType.Hatchback, new Date(), MotorType.Diesel, 5, 1000L, 0L));
        cars.add(new CarEntity("BCD123", "Kia", "Modelo S", CarType.Hatchback, new Date(), MotorType.Diesel, 5, 1000L, 0L));

        when(carRepository.findByBrand("Kia")).thenReturn(cars);

        assertThat(carService.getCarsByBrand("Kia")).isEqualTo(cars);
    }
    @Test
    public void whenGetBrands_thenSuccess() {
        List<CarEntity> cars = new ArrayList<>();
        cars.add(new CarEntity("ABC123", "Kia", "Modelo S", CarType.Hatchback, new Date(), MotorType.Diesel, 5, 1000L, 0L));
        cars.add(new CarEntity("BCD123", "Volvo", "Modelo S", CarType.Hatchback, new Date(), MotorType.Diesel, 5, 1000L, 0L));

        when(carRepository.findAll()).thenReturn(cars);

        assertThat(carService.getBrands()).isEqualTo(List.of("Kia", "Volvo"));
    }
    @Test
    public void whenSetBrandBonus_Success() {
        CarEntity car1 = new CarEntity("ABC123", "Kia", "Modelo S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1000L, 0L);
        CarEntity car2 = new CarEntity("ABC123", "Kia", "Modelo S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 1000L, 1000L);
        when(carRepository.findByPatent("ABC123")).thenReturn(car1);
        assertThat(carService.setBrandBonus("ABC123", 1000L)).isEqualTo(car2);
    }
}
