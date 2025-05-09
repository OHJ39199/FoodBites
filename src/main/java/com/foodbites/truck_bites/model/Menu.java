package com.foodbites.truck_bites.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa el menú de un food truck.
 */
@Entity
@Table(name = "menus")
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck; // Relación 1:1 con FoodTruck

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private Double precio;
}
