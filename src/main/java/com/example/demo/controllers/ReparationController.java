package com.example.demo.controllers;

import com.example.demo.entities.ReparationEntity;
import com.example.demo.services.ReparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reparation")
@CrossOrigin("*")
public class ReparationController {
    @Autowired
    ReparationService reparationService;

    @GetMapping("/")
    public ResponseEntity<Map<String, List<Map>>> getReparations() {
        List<ReparationEntity> newReparations = reparationService.getReparations();
        List<Map> response = new ArrayList<>();
        for (ReparationEntity reparation: newReparations) {
            System.out.println(reparationService.getReparationTypes(reparation));
            response.add(Map.of("patent", reparation.getPatent(),
                    "admissionDate", reparation.getAdmissionDate(),
                    "reparationTypes", reparationService.getReparationTypes(reparation).toString(),
                    "repairExitDate", reparation.getRepairExitDate(),
                    "retrievalDate", reparation.getRetrievalDate()));
        }
        return ResponseEntity.ok(Map.of("reparations", response));
    }

    @PostMapping("/")
    public ResponseEntity<?> saveReparation(@RequestBody ReparationEntity reparationEntity) {

        ReparationEntity newReparation = null;
        try {
            newReparation = reparationService.saveReparation(reparationEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(newReparation);
    }
}
