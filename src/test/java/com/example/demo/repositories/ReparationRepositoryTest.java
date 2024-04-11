package com.example.demo.repositories;

import com.example.demo.entities.ReparationEntity;
import com.example.demo.enums.ReparationType;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class ReparationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ReparationRepository reparationRepository;
    @Test
    public void whenSavingReparationWithType_ThenSuccess() {
        ReparationEntity reparation = new ReparationEntity();
        reparation.setPatent("ABCD12");
        reparation.setReparationType(ReparationType.Combustible);
        reparation.setAdmissionDate(new Date());
        reparation.setRetrievalDate(new Date());
        reparation.setRepairExitDate(new Date());

        entityManager.persistAndFlush(reparation);

        List<ReparationEntity> found = reparationRepository.findByPatent("ABCD12");
        Assertions.assertThat(found.get(0).getReparationTypes().stream().toList().get(0).getReparationType()).isEqualTo(ReparationType.Combustible);
    }
}
