package com.example.demo.repositories;

import com.example.demo.entities.CarEntity;
import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, String> {
    CarEntity findByPatent(String patent);
    List<CarEntity> findByCarType(CarType carType);
    List<CarEntity> findByMotorType(MotorType motorType);
    List<CarEntity> findByBrand(String brand);
}
