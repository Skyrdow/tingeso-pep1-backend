package com.example.demo.services;

import com.example.demo.PriceConstants;
import com.example.demo.entities.CarEntity;
import com.example.demo.entities.ReparationEntity;
import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.enums.ReparationType;
import com.example.demo.repositories.CarRepository;
import com.example.demo.repositories.ReparationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    CarService carService;
    @Autowired
    ReparationService reparationService;
    @Autowired
    ReparationRepository reparationRepository;
    @Autowired
    CarRepository carRepository;
    PriceConstants pc = PriceConstants.getInstance();

    public List<Map> getReport1() {
        List<Map> responseMaps = new ArrayList<>();
        List<CarEntity> everyCar = carService.getCars();
        for (CarEntity car: everyCar) {
            try {
                List<ReparationEntity> reparations = reparationRepository.findByPatent(car.getPatent());
                if (reparations.isEmpty()) continue;
                Long price = getReparationPrices(reparations, car);
                Map newMap = Map.of(
                    "car", car.getPatent(),
                    "totalPrice", calculateReparationPrice(car),
                    "basePrice", getReparationPrices(reparations, car),
                    "surcharges", getSurcharges(reparations, car, price),
                    "discounts", getDiscounts(reparations, car, price),
                    "iva", pc.iva
                );
                responseMaps.add(newMap);
            } catch (Exception e) { responseMaps.add(Map.of("Problem on car:", car.getPatent(),
                    "problem:", e.getMessage())); }
        }
        return responseMaps;
    }

    public Map<CarType, Map> getReport2() {
        Map<CarType, Map> carTypeCounts = new HashMap<>();
        for (CarType carType: CarType.values()) {
            Map<ReparationType, Map> carTypeData = new HashMap<>();
            List<CarEntity> everyCar = carService.getCarsByCarType(carType);
            for (CarEntity car: everyCar) {
                List<ReparationEntity> reparations = reparationService.getReparationsByPatent(car.getPatent());
                for (ReparationEntity reparation: reparations) {
                    List<ReparationType> types = reparationService.getReparationTypes(reparation);
                    for (ReparationType type : types) {
                        Long price = pc.repairPricesMap.get(type).getPrice(car.getMotorType());
                        if (!carTypeData.containsKey(type)) {
                            carTypeData.put(type, Map.of("count", 1, "totalPrice", price));
                        } else {
                            Map accumulator = carTypeData.get(type);
                            Integer newCount = (Integer) accumulator.get("count")+1;
                            Long newPrice = (Long) accumulator.get("totalPrice")+price;
                            carTypeData.put(type, Map.of("count", newCount, "totalPrice", newPrice));
                        }
                    }
                }
            }
            carTypeCounts.put(carType, carTypeData);
        }
        System.out.println(carTypeCounts);
        return carTypeCounts;
    }

    public Map getReport3() {
        Map<String, Long> times = new HashMap<>();
        List<String> brands = carService.getBrands();
        for (String brand: brands) {
            List<CarEntity> brandedCars = carService.getCarsByBrand(brand);
            Long timeAccumulator = 0L;
            Long sizeAccumulator = 0L;
            for (CarEntity brandedCar: brandedCars) {
                List<ReparationEntity> brandedReparations = reparationRepository.findByPatent(brandedCar.getPatent());
                for (ReparationEntity brandedReparation: brandedReparations) {
                    timeAccumulator += reparationService.getReparationTime(brandedReparation);
                    sizeAccumulator++;
                }
            }
            Long meanTime = timeAccumulator / sizeAccumulator;
            times.put(brand, meanTime);
        }
        return times;
    }

    public Map<MotorType, Map> getReport4() {
        Map<MotorType, Map> motorTypeCounts = new HashMap<>();
        for (MotorType motorType: MotorType.values()) {
            Map<ReparationType, Map> motorTypeData = new HashMap<>();
            List<CarEntity> everyCar = carService.getCarsByMotorType(motorType);
            for (CarEntity car: everyCar) {
                List<ReparationEntity> reparations = reparationService.getReparationsByPatent(car.getPatent());
                for (ReparationEntity reparation: reparations) {
                    List<ReparationType> types = reparationService.getReparationTypes(reparation);
                    for (ReparationType type : types) {
                        Long price = pc.repairPricesMap.get(type).getPrice(car.getMotorType());
                        if (!motorTypeData.containsKey(type)) {
                            motorTypeData.put(type, Map.of("count", 1, "totalPrice", price));
                        } else {
                            Map accumulator = motorTypeData.get(type);
                            Integer newCount = (Integer) accumulator.get("count")+1;
                            Long newPrice = (Long) accumulator.get("totalPrice")+price;
                            motorTypeData.put(type, Map.of("count", newCount, "totalPrice", newPrice));
                        }
                    }
                }
            }
            motorTypeCounts.put(motorType, motorTypeData);
        }
        return motorTypeCounts;
    }

    public Float calculateReparationPrice(CarEntity car) throws Exception {
        List<ReparationEntity> reparations = reparationRepository.findByPatent(car.getPatent());
        // Costo Total = [Suma(Reparaciones) + Recargos – Descuentos] + IVA
        try {
            Long price = getReparationPrices(reparations, car);
            Float surcharges = getSurcharges(reparations, car, price);
            Float discounts = getDiscounts(reparations, car, price);
            return applyIva(price + surcharges - discounts);
        } catch (Exception e) { throw new Exception(e.getMessage()); }
    }

    public Long getReparationPrices(List<ReparationEntity> reparations, CarEntity car) {
        Long priceSum = 0L;
        for (ReparationEntity reparation : reparations) {
            for (ReparationType reparationType : reparationService.getReparationTypes(reparation)) {
                priceSum += pc.repairPricesMap.get(reparationType).getPrice(car.getMotorType());
            }
        }
        return priceSum;
    }

    public Float getSurcharges(List<ReparationEntity> reparations, CarEntity car, Long price) throws Exception {
        try {
            Float mileageSurcharge = pc.mileageSurchargeMap.get(car.getCarType()).getMileageSurcharge(car.getMileage())/100f;
            Float ageSurcharge = pc.ageSurchargeMap.get(car.getCarType()).getAgeSurcharge(car.getFabDate())/100f;
            Integer delaySum = 0;
            for (ReparationEntity reparation: reparations) {
             delaySum += pc.delaySurcharge.getDelaySurcharge(reparation.getRepairExitDate(), reparation.getRetrievalDate());
            }
            Float delaySurcharge = delaySum/100f;
            return (mileageSurcharge + ageSurcharge + delaySurcharge) * price;
        } catch (Exception e) { throw new Exception(e.getMessage()); }
    }

    public Float getDiscounts(List<ReparationEntity> reparations, CarEntity car, Long price) throws Exception {
        try {
            Integer reparationCount = reparationService.reparationOnLast12MonthsCount(car);
            Float repairDiscount = pc.discountByRepairMap.get(car.getMotorType()).getDiscountByRepair(reparationCount)/100f;
            Integer daySum = 0;
            for (ReparationEntity reparation: reparations) {
                daySum += pc.discountByDay.getDiscountByDay(reparation.getAdmissionDate());
            }
            Float dayDiscount = daySum/100f;
            Long brandBonus = car.getBrandBonus();

            return ((repairDiscount + dayDiscount) * price) + brandBonus;
        } catch (Exception e) { throw new Exception(e.getMessage()); }
    }

    public Float applyIva(Float price) {
        return (price + (price * pc.iva));
    }
}
