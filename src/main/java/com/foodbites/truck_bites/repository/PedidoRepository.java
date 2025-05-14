package com.foodbites.truck_bites.repository;

import com.foodbites.truck_bites.model.Pedido;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Pedido, gestiona operaciones CRUD y consultas personalizadas.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    /**
     * Encuentra todos los pedidos asociados a un food truck.
     *
     * @param foodTruckId ID del food truck.
     * @return Lista de pedidos del food truck especificado.
     */
    List<Pedido> findByFoodTruckId(Long foodTruckId);

    /**
     * Encuentra todos los pedidos asociados a un usuario.
     *
     * @param usuarioId ID del usuario.
     * @return Lista de pedidos del usuario especificado.
     */
    List<Pedido> findByUsuarioId(Long usuarioId);

    /**
     * Encuentra pedidos pendientes de un food truck.
     *
     * @param foodTruckId ID del food truck.
     * @return Lista de pedidos en estado PENDIENTE.
     */
    @Query("SELECT p FROM Pedido p WHERE p.foodTruck.id = :foodTruckId AND p.estado = 'PENDIENTE'")
    List<Pedido> findPendingByFoodTruckId(@Param("foodTruckId") Long foodTruckId);

    /**
     * Calcula el total de ventas de un food truck en un período.
     *
     * @param foodTruckId ID del food truck.
     * @param startDate   Fecha de inicio.
     * @param endDate     Fecha de fin.
     * @return Suma del monto total de los pedidos.
     */
    @Query("SELECT SUM(p.montoTotal) FROM Pedido p WHERE p.foodTruck.id = :foodTruckId " +
            "AND p.fechaCreacion BETWEEN :startDate AND :endDate")
    Double calculateTotalSalesByFoodTruck(@Param("foodTruckId") Long foodTruckId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * Encuentra pedidos por usuario y food truck.
     *
     * @param usuarioId   ID del usuario.
     * @param foodTruckId ID del food truck.
     * @return Lista de pedidos.
     */
    @Query("SELECT p FROM Pedido p JOIN p.usuario u JOIN p.foodTruck ft WHERE u.id = :usuarioId AND ft.id = :foodTruckId")
    List<Pedido> findByUsuarioIdAndFoodTruckId(@Param("usuarioId") Long usuarioId, @Param("foodTruckId") Long foodTruckId);

    /**
     * Encuentra los food trucks más populares por cantidad de pedidos.
     *
     * @param pageable Límite de resultados.
     * @return Lista de food trucks y su conteo de pedidos.
     */

    @Query("SELECT p.foodTruck, COUNT(p) as orderCount " +
            "FROM Pedido p " +
            "GROUP BY p.foodTruck " +
            "ORDER BY orderCount DESC")
    List<Object[]> findTopFoodTrucksByOrderCount(Pageable pageable);

    @Query("SELECT AVG(p.montoTotal) FROM Pedido p WHERE p.foodTruck.id = :foodTruckId AND p.estado = 'COMPLETADO'")
    Double calculateAverageProfitByFoodTruck(@Param("foodTruckId") Long foodTruckId);

    @Query(value = "WITH RECURSIVE Numbers AS ( " +
            "  SELECT a.N + b.N * 10 + 1 AS n " +
            "  FROM (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) a, " +
            "       (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) b " +
            "), " +
            "MenuCounts AS ( " +
            "  SELECT p.food_truck_id, ft.nombre AS food_truck_nombre, " +
            "         TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(p.items, ',', n.n), ',', -1)) AS menu_item, " +
            "         COUNT(*) AS item_count " +
            "  FROM pedidos p " +
            "  JOIN food_trucks ft ON p.food_truck_id = ft.id " +
            "  JOIN Numbers n ON n.n <= (LENGTH(p.items) - LENGTH(REPLACE(p.items, ',', '')) + 1) " +
            "  WHERE p.estado = 'COMPLETADO' " +
            "  GROUP BY p.food_truck_id, ft.nombre, menu_item " +
            "), " +
            "RankedMenuCounts AS ( " +
            "  SELECT food_truck_id, food_truck_nombre, menu_item, item_count, " +
            "         ROW_NUMBER() OVER (PARTITION BY food_truck_id ORDER BY item_count DESC) AS rn " +
            "  FROM MenuCounts " +
            "), " +
            "TopCustomers AS ( " +
            "  SELECT p.food_truck_id, u.nombre AS customer_name, COUNT(*) AS order_count " +
            "  FROM pedidos p " +
            "  JOIN usuarios u ON p.usuario_id = u.id " +
            "  JOIN RankedMenuCounts rmc ON p.food_truck_id = rmc.food_truck_id " +
            "  WHERE p.estado = 'COMPLETADO' " +
            "  AND p.items LIKE CONCAT('%', rmc.menu_item, '%') AND rmc.rn = 1 " +
            "  GROUP BY p.food_truck_id, u.nombre " +
            "), " +
            "RankedTopCustomers AS ( " +
            "  SELECT food_truck_id, customer_name, order_count, " +
            "         ROW_NUMBER() OVER (PARTITION BY food_truck_id ORDER BY order_count DESC) AS rn " +
            "  FROM TopCustomers " +
            ") " +
            "SELECT rmc.food_truck_id, rmc.food_truck_nombre, rmc.menu_item, rmc.item_count, " +
            "       rtc.customer_name, rtc.order_count " +
            "FROM RankedMenuCounts rmc " +
            "JOIN RankedTopCustomers rtc ON rmc.food_truck_id = rtc.food_truck_id " +
            "WHERE rmc.rn = 1 AND rtc.rn = 1 " +
            "ORDER BY rmc.food_truck_id", nativeQuery = true)
    List<Object[]> findMostConsumedMenuItemsForAllFoodTrucks();
}