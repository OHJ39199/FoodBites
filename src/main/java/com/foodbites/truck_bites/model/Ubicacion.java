package com.foodbites.truck_bites.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad que representa una ubicación registrada de un food truck.
 */
@Entity
@Table(name = "ubicaciones")
@Data
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    @Column(nullable = false)
    private String coordenadas; // Latitud y longitud (puede ser un string o JSON)

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
