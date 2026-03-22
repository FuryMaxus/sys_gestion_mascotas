package com.sanosysalvos.servicio_gestion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = true)
    private String run;

    @Column(nullable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Double size;

    @Column(nullable = true)
    private String foundLocation;

    @Column(nullable = true)
    private String lostLocation;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private UUID ownerId;
}
