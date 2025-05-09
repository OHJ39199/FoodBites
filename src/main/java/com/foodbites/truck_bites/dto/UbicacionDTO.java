package com.foodbites.truck_bites.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para la entidad Ubicacion, utilizado para transferir datos de ubicaciones en la API.
 */
@Data
public class UbicacionDTO {
    private Long id;
    private Long foodTruckId;
    private String coordenadas;
    private LocalDateTime fechaRegistro;
}
