package com.sanosysalvos.servicio_gestion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table()
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una mascota en el sistema de rescate y gestión.")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único de la mascota", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @Column(nullable = true)
    @Schema(description = "Nombre de la mascota", example = "Firulais")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado actual de la mascota", example = "LOST")
    private Status status;

    @Column(nullable = false)
    @Schema(description = "Especie de la mascota", example = "Perro")
    private String species;

    @Column(nullable = false)
    @Schema(description = "Color predominante", example = "Café con manchas blancas")
    private String color;

    @Column(nullable = false)
    @Schema(description = "Tamaño aproximado en centímetros o kilos", example = "15.5")
    private Double size;

    @Column(nullable = true)
    @Schema(description = "Ubicación donde fue encontrada (si aplica)", example = "Plaza de Armas")
    private String foundLocation;

    @Column(nullable = true)
    @Schema(description = "Última ubicación donde fue vista (si aplica)", example = "Calle Arturo Prat 123")
    private String lostLocation;

    @Column(nullable = true)
    @Schema(description = "Características distintivas o notas adicionales", example = "Tiene un collar azul sin placa")
    private String description;

    @Column(nullable = true)
    @Schema(description = "ID interno del dueño registrado en el sistema", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID ownerId;
}
