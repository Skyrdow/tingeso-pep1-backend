package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    @Autowired
    CarRepository carRepository;
    public List<CarEntity> getCars() { return carRepository.findAll();}
    public CarEntity saveCar(CarEntity carEntity) { return carRepository.save(carEntity); }
    public List<CarEntity> getCarsByCarType(CarType carType) { return carRepository.findByCarType(carType); }
    public List<CarEntity> getCarsByMotorType(MotorType motorType) { return carRepository.findByMotorType(motorType); }
    public List<CarEntity> getCarsByBrand(String brand) { return carRepository.findByBrand(brand); }
    public List<String> getBrands() {
        return carRepository.findAll().stream().map(CarEntity::getBrand).distinct().toList();
    }
    public CarEntity setBrandBonus(String patent, Long bonus) {
        CarEntity car = carRepository.findByPatent(patent);
        car.setBrandBonus(bonus);
        return car;
    }
}
