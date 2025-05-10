package com.foodbites.truck_bites.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para la entidad Pedido, utilizado para transferir datos de pedidos en la API.
 */
@Data
public class PedidoDTO {
    private Long id;
    private Long usuarioId;
    private Long foodTruckId;
    private String foodTruckNombre;
    private String items;
    private Double montoTotal;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaProgramada;
}