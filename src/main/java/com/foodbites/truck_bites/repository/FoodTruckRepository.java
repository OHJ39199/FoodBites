package com.foodbites.truck_bites.repository;

import com.foodbites.truck_bites.model.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones CRUD de FoodTruck.
 */
@Repository
public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long> {
    List<FoodTruck> findByUbicacionActualContainingIgnoreCase(String ciudad);
}
