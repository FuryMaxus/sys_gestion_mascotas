package com.sanosysalvos.servicio_gestion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = true)
    private String run;

    @Column(nullable = true)
    private String name;

    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    private Species species;

    @Column(nullable = false)
    private String color;

}
