package com.foodbites.truck_bites.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un food truck en el sistema Truck_bites.
 */
@Entity
@Table(name = "food_trucks")
@Getter
@Setter
public class FoodTruck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "tipo_cocina")
    private String tipoCocina;

    @Column(name = "ubicacion_actual")
    private String ubicacionActual; // Formato: "ciudad, calle"
}
