package com.example.demo.entities;

import com.example.demo.enums.ReparationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tipos_reparaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReparationTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reparation")
    private ReparationEntity reparation;
    private Long reparationId;
    private ReparationType reparationType;
}
