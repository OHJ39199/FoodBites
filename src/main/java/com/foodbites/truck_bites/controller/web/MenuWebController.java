package com.foodbites.truck_bites.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador web para manejar solicitudes relacionadas con las páginas de menú,
 * como la lista principal de menús y la página de administración de menús.
 * Todas las operaciones de datos son manejadas por la API del MenuController existente.
 */
@Controller
public class MenuWebController {

    /**
     * Maneja las solicitudes GET para mostrar la página pública de listado de menús.
     * El JavaScript del frontend obtendrá los datos de los menús de /api/menus.
     * @return El nombre de la plantilla Thymeleaf para la página de menú.
     */
    @GetMapping("/menu")
    public String showMenuPage() {
        return "menu";
    }

    /**
     * Maneja las solicitudes GET para mostrar la página de administración para gestionar menús.
     * El JavaScript del frontend interactuará con /api/menus para operaciones CRUD.
     * @return El nombre de la plantilla Thymeleaf para la página de administración de menús.
     */
    @GetMapping("/adminMenu")
    public String showAdminMenuPage() {
        return "adminMenu";
    }
}
