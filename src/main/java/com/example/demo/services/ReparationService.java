package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.ReparationEntity;
import com.example.demo.entities.ReparationTypeEntity;
import com.example.demo.enums.ReparationType;
import com.example.demo.repositories.CarRepository;
import com.example.demo.repositories.ReparationRepository;
import com.example.demo.repositories.ReparationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReparationService {
    @Autowired
    ReparationRepository reparationRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    ReparationTypeRepository reparationTypeRepository;

    public List<ReparationEntity> getReparations() {
        return reparationRepository.findAll();
    }
    public List<ReparationEntity> getReparationsByPatent(String patent) {
        return reparationRepository.findByPatent(patent);
    }
    public ReparationEntity saveReparation(ReparationEntity reparationEntity) throws Exception {
        ReparationEntity savedEntity = reparationRepository.save(reparationEntity);
        System.out.println(savedEntity);
        if (savedEntity.getReparationTypes().isEmpty()) throw new Exception("Reparation without type");
        for (ReparationTypeEntity rep:savedEntity.getReparationTypes()) {
            rep.setReparationId(savedEntity.getId());
            reparationTypeRepository.save(rep);
        }
        return savedEntity;
    }

    public List<ReparationType> getReparationTypes(ReparationEntity reparation) {
        List<ReparationTypeEntity> reparationTypes = reparationTypeRepository.findByReparationId(reparation.getId());

        return reparationTypes.stream().map(ReparationTypeEntity::getReparationType).toList();
    }
    // Usando fecha de admisión y contando TODAS las reparaciones
    public Integer reparationOnLast12MonthsCount(CarEntity carEntity) {
        List<ReparationEntity> reparations = reparationRepository.findByPatent(carEntity.getPatent());
        System.out.println(reparations);

        // Calcula la fecha de hace 12 meses
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -12);
        Date twelveMonthsAgo = cal.getTime();

        // Filtra las reparaciones en el rango de los últimos 12 meses y cuenta los resultados
        long count = reparations.stream()
                .filter(reparation -> reparation.getAdmissionDate().after(twelveMonthsAgo))
                .count();

        return Math.toIntExact(count);
    }
    public Long getReparationTime(ReparationEntity reparation) {
        Date startDate = reparation.getAdmissionDate();
        Date endDate = reparation.getRepairExitDate();
        return endDate.getTime() - startDate.getTime();
    }
}
