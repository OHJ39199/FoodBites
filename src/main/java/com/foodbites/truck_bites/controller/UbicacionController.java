package com.foodbites.truck_bites.controller;

import com.foodbites.truck_bites.dto.UbicacionDTO;
import com.foodbites.truck_bites.service.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con ubicaciones.
 */
@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    // Crear una nueva ubicación
    @PostMapping
    public ResponseEntity<UbicacionDTO> crearUbicacion(@Valid @RequestBody UbicacionDTO ubicacionDTO) {
        return ResponseEntity.ok(ubicacionService.crearUbicacion(ubicacionDTO));
    }

    // Obtener todas las ubicaciones
    @GetMapping
    public ResponseEntity<List<UbicacionDTO>> obtenerUbicaciones() {
        return ResponseEntity.ok(ubicacionService.obtenerUbicaciones());
    }

    // Obtener una ubicación por ID
    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> obtenerUbicacionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ubicacionService.obtenerUbicacionPorId(id));
    }

    // Actualizar una ubicación
    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDTO> actualizarUbicacion(@PathVariable Long id, @Valid @RequestBody UbicacionDTO ubicacionDTO) {
        return ResponseEntity.ok(ubicacionService.actualizarUbicacion(id, ubicacionDTO));
    }

    // Eliminar una ubicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUbicacion(@PathVariable Long id) {
        ubicacionService.eliminarUbicacion(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener ubicaciones recientes por food truck
    @GetMapping("/foodtruck/{foodTruckId}")
    public ResponseEntity<List<UbicacionDTO>> obtenerUbicacionesRecientes(@PathVariable Long foodTruckId) {
        return ResponseEntity.ok(ubicacionService.obtenerUbicacionesRecientes(foodTruckId));
    }
}
