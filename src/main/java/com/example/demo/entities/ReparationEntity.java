package com.example.demo.entities;


import com.example.demo.enums.ReparationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "reparaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReparationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String patent;
    private Date admissionDate;

    @OneToMany(mappedBy = "reparation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReparationTypeEntity> reparationTypes = new HashSet<>();
    private Date repairExitDate;
    private Date retrievalDate;

    public void setReparationType(ReparationType reparationType) {
        ReparationTypeEntity reparationTypeEntity = new ReparationTypeEntity();
        reparationTypeEntity.setReparationType(reparationType);
        reparationTypeEntity.setReparationId(this.getId());
        reparationTypes.add(reparationTypeEntity);
    }
}
