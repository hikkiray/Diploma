package com.omgtu.medhelper.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "medicines")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String shortDescription;

    @Column(length = 4000)
    private String description;

    @Column(length = 1000)
    private String dosage;

    @Column(length = 2000)
    private String interactions;
} 