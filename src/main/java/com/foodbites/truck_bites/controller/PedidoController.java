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

    @PostMapping
    public ResponseEntity<PedidoDTO> crearPedido(@RequestBody PedidoDTO pedidoDTO) {
        PedidoDTO created = pedidoService.crearPedido(pedidoDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> obtenerPedidos() {
        return ResponseEntity.ok(pedidoService.obtenerPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> actualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.actualizarPedido(id, pedidoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pendientes/{foodTruckId}")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosPendientes(@PathVariable Long foodTruckId) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPendientes(foodTruckId));
    }

    @GetMapping("/ventas/{foodTruckId}")
    public ResponseEntity<Double> obtenerVentasTotales(
            @PathVariable Long foodTruckId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return ResponseEntity.ok(pedidoService.obtenerVentasTotales(foodTruckId, start, end));
    }

    @GetMapping("/usuario/{usuarioId}/foodtruck/{foodTruckId}")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosPorUsuarioYFoodTruck(
            @PathVariable Long usuarioId, @PathVariable Long foodTruckId) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorUsuarioYFoodTruck(usuarioId, foodTruckId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosPorUsuario(@PathVariable Long usuarioId) {
        List<PedidoDTO> pedidos = pedidoService.obtenerPedidosPorUsuario(usuarioId);
        return ResponseEntity.ok(pedidos);
    }
}