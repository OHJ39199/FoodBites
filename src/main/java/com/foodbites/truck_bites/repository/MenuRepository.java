package com.foodbites.truck_bites.repository;

import com.foodbites.truck_bites.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad Menu, gestiona operaciones CRUD y consultas personalizadas.
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {
    /**
     * Encuentra todos los menús asociados a un food truck.
     * @param foodTruckId ID del food truck.
     * @return Lista de menús del food truck especificado.
     */
    List<Menu> findByFoodTruckId(Long foodTruckId);
}
