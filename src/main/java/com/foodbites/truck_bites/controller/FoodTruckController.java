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
    /**
     * Endpoint para obtener food trucks cercanos a una ciudad y calle.
     *
     * @param ciudad Ciudad donde se encuentran los food trucks.
     * @param calle  Calle donde se encuentran los food trucks.
     * @return Lista de food trucks cercanos.
     */
    @GetMapping("/cerca")
    public ResponseEntity<List<FoodTruckDTO>> obtenerFoodTrucksCerca(
            @RequestParam String ciudad,
            @RequestParam(required = false) String calle) {
        List<FoodTruckDTO> foodTrucks = foodTruckService.obtenerFoodTrucksCerca(ciudad, calle);
        return ResponseEntity.ok(foodTrucks);
    }
    /**
     * Endpoint para recomendar food trucks según la ciudad y el tipo de cocina.
     *
     * @param ciudad       Ciudad donde se encuentran los food trucks.
     * @param tipoCocina   Tipo de cocina que se desea.
     * @return Lista de food trucks recomendados.
     */
    @GetMapping("/recomendar")
    public ResponseEntity<List<FoodTruckDTO>> recomendarFoodTrucks(
            @RequestParam String ciudad, @RequestParam String tipoCocina) {
        List<FoodTruckDTO> foodTrucks = foodTruckService.recomendarFoodTrucks(ciudad, tipoCocina);
        return ResponseEntity.ok(foodTrucks);
    }
    /**
     * Endpoint para obtener los food trucks más populares por número de pedidos.
     *
     * @param limit Número máximo de food trucks a devolver.
     * @return Lista de food trucks más populares.
     */
    @GetMapping("/top-by-orders")
    public ResponseEntity<List<FoodTruckDTO>> obtenerTopFoodTrucksPorPedidos(
            @RequestParam(defaultValue = "3") int limit) {
        List<FoodTruckDTO> foodTrucks = foodTruckService.obtenerTopFoodTrucksPorPedidos(limit);
        return ResponseEntity.ok(foodTrucks);
    }

    @GetMapping("/{id}/average-profit")
    public ResponseEntity<Double> obtenerBeneficioMedioPorPedido(@PathVariable Long id) {
        Double averageProfit = foodTruckService.obtenerBeneficioMedioPorPedido(id);
        return ResponseEntity.ok(averageProfit);
    }
}