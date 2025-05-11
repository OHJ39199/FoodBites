package com.foodbites.truck_bites.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa el menú de un food truck.
 */
@Entity
@Table(name = "menus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(name = "imagen_url")
    private String imagenUrl;
}
