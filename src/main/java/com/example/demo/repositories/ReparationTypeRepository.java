package com.example.demo.repositories;

import com.example.demo.entities.ReparationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReparationTypeRepository extends JpaRepository<ReparationTypeEntity, Long> {
    List<ReparationTypeEntity> findByReparationId(Long id);
}
