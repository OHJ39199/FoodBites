package com.foodbites.truck_bites.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para transferir datos de food trucks.
 */
@Getter
@Setter
public class FoodTruckDTO {
    private Long id;
    private String nombre;
    private String tipoCocina;
    private String ubicacionActual; // Formato: "ciudad, calle"
}
