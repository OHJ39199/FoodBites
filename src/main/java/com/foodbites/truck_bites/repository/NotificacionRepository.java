package com.foodbites.truck_bites.repository;

import com.foodbites.truck_bites.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad Notificacion, gestiona operaciones CRUD y consultas personalizadas.
 */
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    /**
     * Encuentra todas las notificaciones asociadas a un usuario.
     * @param usuarioId ID del usuario.
     * @return Lista de notificaciones del usuario especificado.
     */
    List<Notificacion> findByUsuarioId(Long usuarioId);
}
