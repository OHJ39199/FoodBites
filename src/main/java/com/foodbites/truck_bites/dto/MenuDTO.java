package com.foodbites.truck_bites.dto;

import lombok.Data;

/**
 * DTO para la entidad Menu, utilizado para transferir datos de menús en la API.
 */
@Data
public class MenuDTO {
    private Long id;
    private Long foodTruckId;
    private String nombre;
    private String descripcion;
    private Double precio;
}

