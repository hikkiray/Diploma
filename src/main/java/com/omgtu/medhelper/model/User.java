package com.omgtu.medhelper.model;

import jakarta.persistence.*;
import lombok.Data;

import java.text.DateFormat;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String telegramId;

    @Column(nullable = false)
    private String username;

    @Column
    private LocalTime created_at;

    @Column
    private String email;

    @Column
    private String name;
} 