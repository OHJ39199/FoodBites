package com.foodbites.truck_bites.controller;

import com.foodbites.truck_bites.dto.NotificacionDTO;
import com.foodbites.truck_bites.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con notificaciones.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    // Crear una nueva notificación
    @PostMapping
    public ResponseEntity<NotificacionDTO> crearNotificacion(@Valid @RequestBody NotificacionDTO notificacionDTO) {
        return ResponseEntity.ok(notificacionService.crearNotificacion(notificacionDTO));
    }

    // Obtener todas las notificaciones
    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificaciones() {
        return ResponseEntity.ok(notificacionService.obtenerNotificaciones());
    }

    // Obtener una notificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDTO> obtenerNotificacionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionPorId(id));
    }

    // Actualizar una notificación
    @PutMapping("/{id}")
    public ResponseEntity<NotificacionDTO> actualizarNotificacion(@PathVariable Long id, @Valid @RequestBody NotificacionDTO notificacionDTO) {
        return ResponseEntity.ok(notificacionService.actualizarNotificacion(id, notificacionDTO));
    }

    // Eliminar una notificación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener notificaciones por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificacionesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesPorUsuario(usuarioId));
    }
}
