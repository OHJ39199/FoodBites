package com.foodbites.truck_bites.controller;

import com.foodbites.truck_bites.dto.PedidoDTO;
import com.foodbites.truck_bites.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<PedidoDTO> crearPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.crearPedido(pedidoDTO));
    }

    // Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> obtenerPedidos() {
        return ResponseEntity.ok(pedidoService.obtenerPedidos());
    }

    // Obtener un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    // Actualizar un pedido
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> actualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.actualizarPedido(id, pedidoDTO));
    }

    // Eliminar un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener pedidos pendientes por food truck
    @GetMapping("/pendientes/{foodTruckId}")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosPendientes(@PathVariable Long foodTruckId) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPendientes(foodTruckId));
    }

    // Obtener ventas totales por food truck en un período
    @GetMapping("/ventas/{foodTruckId}")
    public ResponseEntity<Double> obtenerVentasTotales(
            @PathVariable Long foodTruckId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return ResponseEntity.ok(pedidoService.obtenerVentasTotales(foodTruckId, start, end));
    }
}
