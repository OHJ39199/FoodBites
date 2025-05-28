package com.foodbites.truck_bites.controller.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador web para manejar solicitudes relacionadas con páginas orientadas al usuario
 * como el registro y la gestión de usuarios. Las operaciones de la API REST para usuarios
 * son manejadas por UsuarioController.
 */
@Controller
public class UserWebController {

    // Nota: No se necesita una dependencia directa de UsuarioService aquí,
    // ya que las llamadas a la API se harán desde JavaScript del frontend a UsuarioController.

    /**
     * Maneja las solicitudes GET para mostrar la página de registro de usuarios.
     * @return El nombre de la plantilla Thymeleaf para la página de registro.
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        // El formulario en register.html enviará sus datos a través de JavaScript
        // al endpoint /api/usuarios manejado por UsuarioController.
        return "register";
    }

    /**
     * Maneja las solicitudes GET para mostrar una página genérica de gestión/listado de usuarios.
     * Esto es un marcador de posición para el enlace "Usuarios" en tu barra de navegación.
     * @return El nombre de la plantilla Thymeleaf para la página de gestión de usuarios.
     */
    @GetMapping("/usuarios")
    public String showUsersPage() {
        return "usuarios"; // Suponiendo que tienes un 'usuarios.html' para listar/gestionar usuarios
    }
}
