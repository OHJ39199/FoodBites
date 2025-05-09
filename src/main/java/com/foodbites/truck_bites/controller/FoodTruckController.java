package com.foodbites.truck_bites.controller;

import com.foodbites.truck_bites.dto.FoodTruckDTO;
import com.foodbites.truck_bites.service.FoodTruckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar operaciones relacionadas con food trucks.
 */
@RestController
@RequestMapping("/api/foodtrucks")
public class FoodTruckController {

    private final FoodTruckService foodTruckService;

    public FoodTruckController(FoodTruckService foodTruckService) {
        this.foodTruckService = foodTruckService;
    }

    @PostMapping
    public ResponseEntity<FoodTruckDTO> crearFoodTruck(@RequestBody FoodTruckDTO foodTruckDTO) {
        FoodTruckDTO created = foodTruckService.crearFoodTruck(foodTruckDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<FoodTruckDTO>> obtenerFoodTrucks() {
        List<FoodTruckDTO> foodTrucks = foodTruckService.obtenerFoodTrucks();
        return ResponseEntity.ok(foodTrucks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodTruckDTO> obtenerFoodTruckPorId(@PathVariable Long id) {
        FoodTruckDTO foodTruck = foodTruckService.obtenerFoodTruckPorId(id);
        return ResponseEntity.ok(foodTruck);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodTruckDTO> actualizarFoodTruck(@PathVariable Long id, @RequestBody FoodTruckDTO foodTruckDTO) {
        FoodTruckDTO updated = foodTruckService.actualizarFoodTruck(id, foodTruckDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFoodTruck(@PathVariable Long id) {
        foodTruckService.eliminarFoodTruck(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cerca")
    public ResponseEntity<List<FoodTruckDTO>> obtenerFoodTrucksCerca(
            @RequestParam String ciudad,
            @RequestParam(required = false) String calle) {
        List<FoodTruckDTO> foodTrucks = foodTruckService.obtenerFoodTrucksCerca(ciudad, calle);
        return ResponseEntity.ok(foodTrucks);
    }

    @GetMapping("/recomendar")
    public ResponseEntity<List<FoodTruckDTO>> recomendarFoodTrucks(
            @RequestParam String ciudad, @RequestParam String tipoCocina) {
        List<FoodTruckDTO> foodTrucks = foodTruckService.recomendarFoodTrucks(ciudad, tipoCocina);
        return ResponseEntity.ok(foodTrucks);
    }
}