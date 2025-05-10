package com.foodbites.truck_bites.repository;

import com.foodbites.truck_bites.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio para la entidad Pedido, gestiona operaciones CRUD y consultas personalizadas.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    /**
     * Encuentra todos los pedidos asociados a un food truck.
     * @param foodTruckId ID del food truck.
     * @return Lista de pedidos del food truck especificado.
     */
    List<Pedido> findByFoodTruckId(Long foodTruckId);

    /**
     * Encuentra todos los pedidos asociados a un usuario.
     * @param usuarioId ID del usuario.
     * @return Lista de pedidos del usuario especificado.
     */
    List<Pedido> findByUsuarioId(Long usuarioId);

    /**
     * Encuentra pedidos pendientes de un food truck.
     * @param foodTruckId ID del food truck.
     * @return Lista de pedidos en estado PENDIENTE.
     */
    @Query("SELECT p FROM Pedido p WHERE p.foodTruck.id = :foodTruckId AND p.estado = 'PENDIENTE'")
    List<Pedido> findPendingByFoodTruckId(@Param("foodTruckId") Long foodTruckId);

    /**
     * Calcula el total de ventas de un food truck en un período.
     * @param foodTruckId ID del food truck.
     * @param startDate Fecha de inicio.
     * @param endDate Fecha de fin.
     * @return Suma del monto total de los pedidos.
     */
    @Query("SELECT SUM(p.montoTotal) FROM Pedido p WHERE p.foodTruck.id = :foodTruckId " +
            "AND p.fechaCreacion BETWEEN :startDate AND :endDate")
    Double calculateTotalSalesByFoodTruck(@Param("foodTruckId") Long foodTruckId,
                                          @Param("startDate") java.time.LocalDateTime startDate,
                                          @Param("endDate") java.time.LocalDateTime endDate);

    /**
     * Encuentra pedidos por usuario y food truck.
     * @param usuarioId ID del usuario.
     * @param foodTruckId ID del food truck.
     * @return Lista de pedidos.
     */
    @Query("SELECT p FROM Pedido p JOIN p.usuario u JOIN p.foodTruck ft WHERE u.id = :usuarioId AND ft.id = :foodTruckId")
    List<Pedido> findByUsuarioIdAndFoodTruckId(@Param("usuarioId") Long usuarioId, @Param("foodTruckId") Long foodTruckId);
}