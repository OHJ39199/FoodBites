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
    public ResponseEntity<MenuDTO> crearMenu(@RequestBody MenuDTO menuDTO) {
        MenuDTO created = menuService.crearMenu(menuDTO);
        return ResponseEntity.ok(created);
    }

    // Obtener todos los menús
    @GetMapping
    public ResponseEntity<List<MenuDTO>> obtenerMenus() {
        List<MenuDTO> menus = menuService.obtenerMenus();
        return ResponseEntity.ok(menus);
    }

    // Obtener un menú por ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> obtenerMenuPorId(@PathVariable Long id) {
        MenuDTO menu = menuService.obtenerMenuPorId(id);
        return ResponseEntity.ok(menu);
    }

    // Actualizar un menú
    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> actualizarMenu(@PathVariable Long id, @RequestBody MenuDTO menuDTO) {
        MenuDTO updated = menuService.actualizarMenu(id, menuDTO);
        return ResponseEntity.ok(updated);
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
        List<MenuDTO> menus = menuService.obtenerMenusPorFoodTruck(foodTruckId);
        return ResponseEntity.ok(menus);
    }
}

