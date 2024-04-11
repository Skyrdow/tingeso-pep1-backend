package com.example.demo.controllers;

import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.enums.ReparationType;
import com.example.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/report")
@CrossOrigin("*")
public class ReportController {
    @Autowired
    ReportService reportService;
    @GetMapping("/1")
    public ResponseEntity<?> getReport1() {
        List<Map> report1 = reportService.getReport1();
        return ResponseEntity.ok(report1);
    }
    @GetMapping("/2")
    public ResponseEntity<?> getReport2() {
        Map<CarType, Map> report2 = reportService.getReport2();
        List<Map> response = new ArrayList<>();
        for (ReparationType reparationType: ReparationType.values()) {
            List<Map> carTypesMaps = new ArrayList<>();
            for (CarType carType:CarType.values()) {
                Map carTypeReport = (Map) report2.get(carType).get(reparationType);
                if (carTypeReport == null) {
                    carTypesMaps.add(Map.of(
                            "carType", carType,
                            "totalPrice", 0,
                            "count", 0));
                } else {
                    carTypesMaps.add(Map.of(
                            "carType", carType,
                            "totalPrice", carTypeReport.get("totalPrice"),
                            "count", carTypeReport.get("count")));
                }
            }
            Map reparationMap = Map.of("reparationType", reparationType,
                    "carTypes", carTypesMaps);
            response.add(reparationMap);
        }
        return ResponseEntity.ok(Map.of("report", response));
    }
    @GetMapping("/3")
    public ResponseEntity<?> getReport3() {
        Map report3 = reportService.getReport3();
        return ResponseEntity.ok(report3);
    }
    @GetMapping("/4")
    public ResponseEntity<?> getReport4() {
        Map<MotorType, Map> report2 = reportService.getReport4();
        List<Map> response = new ArrayList<>();
        for (ReparationType reparationType: ReparationType.values()) {
            List<Map> motorTypesMaps = new ArrayList<>();
            for (MotorType motorType:MotorType.values()) {
                Map carTypeReport = (Map) report2.get(motorType).get(reparationType);
                if (carTypeReport == null) {
                    motorTypesMaps.add(Map.of(
                            "motorType", motorType,
                            "totalPrice", 0,
                            "count", 0));
                } else {
                    motorTypesMaps.add(Map.of(
                            "motorType", motorType,
                            "totalPrice", carTypeReport.get("totalPrice"),
                            "count", carTypeReport.get("count")));
                }
            }
            Map reparationMap = Map.of("reparationType", reparationType,
                    "motorTypes", motorTypesMaps);
            response.add(reparationMap);
        }
        return ResponseEntity.ok(Map.of("report", response));
    }
}
