package com.foodbites.truck_bites.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad que representa un pedido realizado por un usuario, incluyendo pedidos inmediatos y programados.
 */
@Entity
@Table(name = "pedidos")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    @Column(nullable = false)
    private String items; // Lista de ítems pedidos (puede ser JSON o texto)

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    private String estado; // Ejemplo: PENDIENTE, COMPLETADO, PROGRAMADO

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada; // Fecha para pedidos programados (nulo para pedidos inmediatos)
}