package com.example.demo;

import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.enums.ReparationType;

import java.util.HashMap;
import java.util.Map;

public class PriceConstants {
    private static PriceConstants instance = null;
    // Precios por tipo de reparacion
    public Float iva;
    public Map<ReparationType, RepairPrices> repairPricesMap;
    public Map<MotorType, DiscountByRepair> discountByRepairMap;

    public Map<CarType, MileageSurcharge> mileageSurchargeMap;
    public Map<CarType, AgeSurcharge> ageSurchargeMap;
    public DelaySurcharge delaySurcharge;
    public DiscountByDay discountByDay;
    public static PriceConstants getInstance() {
        if (instance == null) {
            instance = new PriceConstants();
        }
        return instance;
    }
    private PriceConstants() {
        repairPricesMap = new HashMap<>();
        repairPricesMap.put(ReparationType.Frenos, new RepairPrices(120000L, 120000L, 180000L, 220000L));
        repairPricesMap.put(ReparationType.Refrigeracion, new RepairPrices(130000L, 130000L, 190000L, 230000L));
        repairPricesMap.put(ReparationType.Motor, new RepairPrices(350000L, 450000L, 700000L, 800000L));
        repairPricesMap.put(ReparationType.Transmision, new RepairPrices(210000L, 210000L, 300000L, 300000L));
        repairPricesMap.put(ReparationType.Electrico, new RepairPrices(150000L, 150000L, 200000L, 250000L));
        repairPricesMap.put(ReparationType.Escape, new RepairPrices(100000L, 120000L, 450000L, 0L));
        repairPricesMap.put(ReparationType.Neumaticos, new RepairPrices(100000L, 100000L, 100000L, 100000L));
        repairPricesMap.put(ReparationType.Suspension, new RepairPrices(180000L, 180000L, 210000L, 250000L));
        repairPricesMap.put(ReparationType.Calefaccion, new RepairPrices(150000L, 150000L, 180000L, 180000L));
        repairPricesMap.put(ReparationType.Combustible, new RepairPrices(130000L, 140000L, 220000L, 0L));
        repairPricesMap.put(ReparationType.Parabrisas, new RepairPrices(80000L, 80000L, 80000L, 80000L));

        discountByRepairMap = new HashMap<>();
        discountByRepairMap.put(MotorType.Gasolina, new DiscountByRepair(5, 10, 15, 20));
        discountByRepairMap.put(MotorType.Diesel, new DiscountByRepair(7, 12, 17, 22));
        discountByRepairMap.put(MotorType.Hibrido, new DiscountByRepair(10, 15, 20, 25));
        discountByRepairMap.put(MotorType.Electrico, new DiscountByRepair(8, 13, 18, 23));

        mileageSurchargeMap = new HashMap<>();
        mileageSurchargeMap.put(CarType.Sedan, new MileageSurcharge(0, 3, 7, 12, 20));
        mileageSurchargeMap.put(CarType.Hatchback, new MileageSurcharge(0, 3, 7, 12, 20));
        mileageSurchargeMap.put(CarType.SUV, new MileageSurcharge(0, 5, 9, 12, 20));
        mileageSurchargeMap.put(CarType.Pickup, new MileageSurcharge(0, 5, 9, 12, 20));
        mileageSurchargeMap.put(CarType.Furgoneta, new MileageSurcharge(0, 5, 9, 12, 20));

        ageSurchargeMap = new HashMap<>();
        ageSurchargeMap.put(CarType.Sedan, new AgeSurcharge(0, 5, 9, 15));
        ageSurchargeMap.put(CarType.Hatchback, new AgeSurcharge(0, 5, 9, 15));
        ageSurchargeMap.put(CarType.SUV, new AgeSurcharge(0, 7, 11, 20));
        ageSurchargeMap.put(CarType.Pickup, new AgeSurcharge(0, 7, 11, 20));
        ageSurchargeMap.put(CarType.Furgoneta, new AgeSurcharge(0, 7, 11, 20));

        delaySurcharge = new DelaySurcharge(5);
        discountByDay = new DiscountByDay(10);

        iva = 0.19f;
    }
}
