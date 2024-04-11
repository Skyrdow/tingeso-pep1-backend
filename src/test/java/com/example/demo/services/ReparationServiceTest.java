package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.ReparationEntity;
import com.example.demo.entities.ReparationTypeEntity;
import com.example.demo.enums.CarType;
import com.example.demo.enums.MotorType;
import com.example.demo.enums.ReparationType;
import com.example.demo.repositories.ReparationRepository;
import com.example.demo.repositories.ReparationTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ReparationServiceTest {

    CarEntity car = new CarEntity();
    ReparationEntity reparation = new ReparationEntity();
    @Mock
    ReparationRepository reparationRepository;
    @Mock
    ReparationTypeRepository reparationTypeRepository;
    @InjectMocks
    ReparationService reparationService;
    @Test
    public void whenGetReparations_Successful() {
        List<ReparationEntity> reparationEntities = new ArrayList<>();
        reparationEntities.add(new ReparationEntity(1L, "ABC123", new Date(), null, new Date(), new Date()));

        when(reparationRepository.findAll()).thenReturn(reparationEntities);

        assertThat(reparationService.getReparations()).isEqualTo(reparationEntities);
    }
    @Test
    public void whenGetReparationsByPatent_Successful() {
        List<ReparationEntity> reparationEntities = new ArrayList<>();
        reparationEntities.add(new ReparationEntity(1L, "ABC123", new Date(), null, new Date(), new Date()));

        when(reparationRepository.findByPatent("ABC123")).thenReturn(reparationEntities);

        assertThat(reparationService.getReparationsByPatent("ABC123")).isEqualTo(reparationEntities);
    }
    @Test
    public void whenSaveReparation_Success() {
        ReparationTypeEntity repType = new ReparationTypeEntity(1L, null, 1L, ReparationType.Frenos);
        reparation = new ReparationEntity(1L, "ABC123", new Date(), Set.of(repType), new Date(), new Date());

        when(reparationRepository.save(reparation)).thenReturn(reparation);
        when(reparationTypeRepository.save(repType)).thenReturn(repType);

        assertDoesNotThrow(() -> {
            ReparationEntity savedReparation = reparationService.saveReparation(reparation);
            assertEquals(reparation, savedReparation);
        });
    }
    @Test
    public void whenSaveReparationWithoutType_Fail() {
        reparation = new ReparationEntity(1L, "ABC123", new Date(), null, new Date(), new Date());

        when(reparationRepository.save(reparation)).thenReturn(reparation);

        assertThrows(Exception.class, () -> {
            // Código que se espera lance ExpectedExceptionType.
            reparationService.saveReparation(reparation);
        });
    }
    @Test
    public void whenGetReparationTypes_Success() {
        ReparationTypeEntity repType1 = new ReparationTypeEntity();
        repType1.setReparationType(ReparationType.Combustible);
        ReparationTypeEntity repType2 = new ReparationTypeEntity();
        repType2.setReparationType(ReparationType.Frenos);
        reparation = new ReparationEntity(1L, "ABC123", new Date(), Set.of(repType1, repType2), new Date(), new Date());

        when(reparationTypeRepository.findByReparationId(reparation.getId())).thenReturn(List.of(repType1, repType2));

        assertThat(reparationService.getReparationTypes(reparation)).isEqualTo(List.of(ReparationType.Combustible, ReparationType.Frenos));
    }
    @Test
    public void whenReparationOnLast12MonthsCount_Success() {
        CarEntity carEntity = new CarEntity("ABC123", "Kia", "Model S", CarType.Sedan, new Date(), MotorType.Diesel, 5, 5000L, 0L);
        ReparationEntity reparation1 = new ReparationEntity(1L, "ABC123", new Date(), null, new Date(), new Date());
        ReparationEntity reparation2 = new ReparationEntity(1L, "ABC123", new Date(), null, new Date(), new Date());

        when(reparationRepository.findByPatent(carEntity.getPatent())).thenReturn(List.of(reparation1, reparation2));
        assertThat(reparationService.reparationOnLast12MonthsCount(carEntity)).isEqualTo(2);
    }
    @Test
    public void whenGetReparationTime_Success() {
        reparation = new ReparationEntity(1L, "ABC123", new Date(100000), null, new Date(120000), new Date(1300000));

        assertThat(reparationService.getReparationTime(reparation)).isEqualTo(20000L);
    }
}
