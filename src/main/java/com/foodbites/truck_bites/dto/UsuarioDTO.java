package com.foodbites.truck_bites.dto;

import lombok.Data;

/**
 * DTO para la entidad Usuario, utilizado para transferir datos de usuarios en la API.
 */
@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String ubicacion;
}
