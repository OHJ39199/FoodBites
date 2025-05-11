package com.foodbites.truck_bites.controller;

import com.foodbites.truck_bites.dto.MenuDTO;
import com.foodbites.truck_bites.service.MenuService;
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

    /**
     * Constructor del controlador de menús.
     *
     * @param menuService Servicio de menús.
     */
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Crea un nuevo menú.
     *
     * @param menuDTO DTO del menú a crear.
     * @return El DTO del menú creado.
     */
    @PostMapping
    public ResponseEntity<MenuDTO> crearMenu(@RequestBody MenuDTO menuDTO) {
        System.out.println("Received menu: " + menuDTO);
        return ResponseEntity.ok(menuService.crearMenu(menuDTO));
    }

    /**
     * Crea múltiples menús.
     *
     * @param menuDTOs Lista de DTOs de menús a crear.
     * @return Lista de DTOs de menús creados.
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<MenuDTO>> crearMenus(@RequestBody List<MenuDTO> menuDTOs) {
        System.out.println("Received menus: " + menuDTOs);
        return ResponseEntity.ok(menuService.crearMenus(menuDTOs));
    }

    /**
     * Obtiene todos los menús.
     *
     * @return Lista de DTOs de menús.
     */
    @GetMapping
    public ResponseEntity<List<MenuDTO>> obtenerMenus() {
        return ResponseEntity.ok(menuService.obtenerMenus());
    }

    /**
     * Obtiene un menú por su ID.
     *
     * @param id ID del menú a obtener.
     * @return El DTO del menú encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> obtenerMenuPorId(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.obtenerMenuPorId(id));
    }

    /**
     * Actualiza un menú.
     *
     * @param id      ID del menú a actualizar.
     * @param menuDTO DTO con los nuevos datos del menú.
     * @return El DTO del menú actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> actualizarMenu(@PathVariable Long id, @RequestBody MenuDTO menuDTO) {
        System.out.println("Updating menu ID " + id + " with: " + menuDTO);
        return ResponseEntity.ok(menuService.actualizarMenu(id, menuDTO));
    }

    /**
     * Elimina un menú por su ID.
     *
     * @param id ID del menú a eliminar.
     * @return Respuesta vacía con estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMenu(@PathVariable Long id) {
        menuService.eliminarMenu(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene todos los menús de un food truck específico.
     *
     * @param foodTruckId ID del food truck.
     * @return Lista de DTOs de menús del food truck.
     */
    @GetMapping("/foodtruck/{foodTruckId}")
    public ResponseEntity<List<MenuDTO>> obtenerMenusPorFoodTruck(@PathVariable Long foodTruckId) {
        return ResponseEntity.ok(menuService.obtenerMenusPorFoodTruck(foodTruckId));
    }
}