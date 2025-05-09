package com.foodbites.truck_bites.controller;

import com.foodbites.truck_bites.dto.MenuDTO;
import com.foodbites.truck_bites.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con menús.
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // Crear un nuevo menú
    @PostMapping
    public ResponseEntity<MenuDTO> crearMenu(@Valid @RequestBody MenuDTO menuDTO) {
        return ResponseEntity.ok(menuService.crearMenu(menuDTO));
    }

    // Obtener todos los menús
    @GetMapping
    public ResponseEntity<List<MenuDTO>> obtenerMenus() {
        return ResponseEntity.ok(menuService.obtenerMenus());
    }

    // Obtener un menú por ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> obtenerMenuPorId(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.obtenerMenuPorId(id));
    }

    // Actualizar un menú
    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> actualizarMenu(@PathVariable Long id, @Valid @RequestBody MenuDTO menuDTO) {
        return ResponseEntity.ok(menuService.actualizarMenu(id, menuDTO));
    }

    // Eliminar un menú
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMenu(@PathVariable Long id) {
        menuService.eliminarMenu(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener menús por food truck
    @GetMapping("/foodtruck/{foodTruckId}")
    public ResponseEntity<List<MenuDTO>> obtenerMenusPorFoodTruck(@PathVariable Long foodTruckId) {
        return ResponseEntity.ok(menuService.obtenerMenusPorFoodTruck(foodTruckId));
    }
}

