package com.foodbites.truck_bites.controller.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador web para manejar solicitudes relacionadas con las páginas de food trucks,
 * como la lista principal de food trucks y la página de administración.
 * Todas las operaciones de datos son manejadas por la API del FoodTruckController existente.
 */
@Controller
public class FoodTruckWebController {

    /**
     * Maneja las solicitudes GET para mostrar la página pública de listado de food trucks.
     * El JavaScript del frontend obtendrá los datos de los food trucks de /api/foodtrucks.
     * @return El nombre de la plantilla Thymeleaf para la página de food trucks.
     */
    @GetMapping("/foodtrucks")
    public String showFoodTrucksPage() {
        return "foodtrucks";
    }

    /**
     * Maneja las solicitudes GET para mostrar la página de administración para gestionar food trucks.
     * El JavaScript del frontend interactuará con /api/foodtrucks para operaciones CRUD.
     * @return El nombre de la plantilla Thymeleaf para la página de administración de food trucks.
     */
    @GetMapping("/adminFoodTrucks")
    public String showAdminFoodTrucksPage() {
        return "adminFoodTrucks";
    }
}
