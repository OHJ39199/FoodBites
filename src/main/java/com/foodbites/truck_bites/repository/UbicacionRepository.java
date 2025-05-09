package com.foodbites.truck_bites.repository;

import com.foodbites.truck_bites.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad Ubicacion, gestiona operaciones CRUD y consultas personalizadas.
 */
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    /**
     * Encuentra todas las ubicaciones asociadas a un food truck, ordenadas por fecha de registro.
     * @param foodTruckId ID del food truck.
     * @return Lista de ubicaciones ordenadas por fecha descendente.
     */
    List<Ubicacion> findByFoodTruckIdOrderByFechaRegistroDesc(Long foodTruckId);
}
