package com.example.demo.controllers;

import com.example.demo.entities.CarEntity;
import com.example.demo.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
@CrossOrigin("*")
public class CarController {
    @Autowired
    CarService carService;

    @GetMapping("/")
    public ResponseEntity<List<CarEntity>> listCars() {
        List<CarEntity> cars = carService.getCars();
        return ResponseEntity.ok(cars);
    }
    @PostMapping("/")
    public ResponseEntity<CarEntity> saveCar(@RequestBody CarEntity carEntity) {
        CarEntity newCarEntity = carService.saveCar(carEntity);
        return ResponseEntity.ok(newCarEntity);
    }
    @PostMapping("/brandBonus/{patent}/{bonus}")
    public ResponseEntity<CarEntity> addBrandBonus(@PathVariable String patent, @PathVariable Long bonus) {
        CarEntity newCarEntity = carService.setBrandBonus(patent, bonus);
        return ResponseEntity.ok(newCarEntity);
    }
}
