package com.foodbites.truck_bites.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador web para manejar solicitudes relacionadas con las notificaciones y pedidos de los usuarios.
 * Todas las operaciones de datos son manejadas por las APIs de NotificacionController y PedidoController existentes.
 */
@Controller
public class NotificationPedidoWebController {

    /**
     * Maneja las solicitudes GET para mostrar la página de notificaciones.
     * El JavaScript del frontend obtendrá las notificaciones de /api/notificaciones/usuario/{userId}.
     * @return El nombre de la plantilla Thymeleaf para la página de notificaciones.
     */
    @GetMapping("/notificaciones")
    public String showNotificationsPage() {
        return "notificaciones";
    }

    /**
     * Maneja las solicitudes GET para mostrar la página de realización/visualización de pedidos.
     * El JavaScript del frontend interactuará con /api/pedidos.
     * @return El nombre de la plantilla Thymeleaf para la página de pedidos.
     */
    @GetMapping("/pedido")
    public String showOrderPage() {
        return "pedido";
    }
}
