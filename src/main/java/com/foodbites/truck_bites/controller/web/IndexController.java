package com.foodbites.truck_bites.controller.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para manejar las solicitudes relacionadas con la página de inicio (index).
 */
@Controller
public class IndexController {

    /**
     * Maneja las solicitudes GET a la URL raíz ("/") y sirve la página index.html.
     * @return El nombre de la plantilla Thymeleaf para la página de inicio.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
