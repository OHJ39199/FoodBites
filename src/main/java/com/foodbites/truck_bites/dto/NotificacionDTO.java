package com.foodbites.truck_bites.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para la entidad Notificacion, utilizado para transferir datos de notificaciones en la API.
 */
@Data
public class NotificacionDTO {
    private Long id;
    private Long usuarioId;
    private String mensaje;
    private LocalDateTime fechaEnvio;
}
