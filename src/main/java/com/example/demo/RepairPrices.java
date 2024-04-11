package com.example.demo;

import com.example.demo.enums.MotorType;
import lombok.Getter;

public class RepairPrices {
    @Getter
    private Long gasolinaPrice;
    @Getter
    private Long dieselPrice;
    @Getter
    private Long hibridoPrice;
    @Getter
    private Long electricoPrice;

    public RepairPrices(Long gasolinaPrice, Long dieselPrice, Long hibridoPrice, Long electricoPrice) {
        this.gasolinaPrice = gasolinaPrice;
        this.dieselPrice = dieselPrice;
        this.hibridoPrice = hibridoPrice;
        this.electricoPrice = electricoPrice;
    }
    public Long getPrice(MotorType motorType) {
        switch (motorType) {
            case Diesel -> {
                return this.getDieselPrice();
            }
            case Hibrido -> {
                return this.getHibridoPrice();
            }
            case Gasolina -> {
                return this.getGasolinaPrice();
            }
            case Electrico -> {
                return this.getElectricoPrice();
            }
            default -> {
                return null;
            }
        }
    }
}