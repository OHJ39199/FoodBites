package com.foodbites.truck_bites.service;

import com.foodbites.truck_bites.dto.FoodTruckDTO;
import com.foodbites.truck_bites.model.FoodTruck;
import com.foodbites.truck_bites.repository.FoodTruckRepository;
import com.foodbites.truck_bites.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con food trucks.
 */
@Service
public class FoodTruckService  {

    private final FoodTruckRepository foodTruckRepository;
    private final PedidoRepository pedidoRepository;

    public FoodTruckService(FoodTruckRepository foodTruckRepository, PedidoRepository pedidoRepository) {
        this.foodTruckRepository = foodTruckRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public FoodTruckDTO crearFoodTruck(FoodTruckDTO foodTruckDTO) {
        FoodTruck foodTruck = new FoodTruck();
        foodTruck.setNombre(foodTruckDTO.getNombre());
        foodTruck.setTipoCocina(foodTruckDTO.getTipoCocina());
        foodTruck.setUbicacionActual(foodTruckDTO.getUbicacionActual());
        foodTruck = foodTruckRepository.save(foodTruck);
        return toDTO(foodTruck);
    }

    public List<FoodTruckDTO> obtenerFoodTrucks() {
        return foodTruckRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public FoodTruckDTO obtenerFoodTruckPorId(Long id) {
        Optional<FoodTruck> foodTruck = foodTruckRepository.findById(id);
        if (foodTruck.isPresent()) {
            return toDTO(foodTruck.get());
        }
        throw new IllegalArgumentException("FoodTruck no encontrado con ID: " + id);
    }

    @Transactional
    public FoodTruckDTO actualizarFoodTruck(Long id, FoodTruckDTO foodTruckDTO) {
        Optional<FoodTruck> foodTruckOpt = foodTruckRepository.findById(id);
        if (foodTruckOpt.isPresent()) {
            FoodTruck foodTruck = foodTruckOpt.get();
            if (foodTruckDTO.getNombre() != null) foodTruck.setNombre(foodTruckDTO.getNombre());
            if (foodTruckDTO.getTipoCocina() != null) foodTruck.setTipoCocina(foodTruckDTO.getTipoCocina());
            if (foodTruckDTO.getUbicacionActual() != null) foodTruck.setUbicacionActual(foodTruckDTO.getUbicacionActual());
            foodTruck = foodTruckRepository.save(foodTruck);
            return toDTO(foodTruck);
        }
        throw new IllegalArgumentException("FoodTruck no encontrado con ID: " + id);
    }

    @Transactional
    public void eliminarFoodTruck(Long id) {
        if (foodTruckRepository.existsById(id)) {
            foodTruckRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("FoodTruck no encontrado con ID: " + id);
        }
    }

    public List<FoodTruckDTO> obtenerFoodTrucksCerca(String ciudad, String calle) {
        String ubicacion = ciudad + (calle != null && !calle.isEmpty() ? ", " + calle : "");
        return foodTruckRepository.findByUbicacionActualContainingIgnoreCase(ciudad)
                .stream()
                .filter(ft -> calle == null || calle.isEmpty() || ft.getUbicacionActual().toLowerCase().contains(calle.toLowerCase()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<FoodTruckDTO> recomendarFoodTrucks(String ciudad, String tipoCocina) {
        return foodTruckRepository.findByUbicacionActualContainingIgnoreCase(ciudad)
                .stream()
                .filter(ft -> ft.getTipoCocina().equalsIgnoreCase(tipoCocina))
                .sorted(Comparator.comparing(FoodTruck::getNombre))
                .limit(5)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<FoodTruckDTO> obtenerTopFoodTrucksPorPedidos(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor que 0");
        }
        List<Object[]> results = pedidoRepository.findTopFoodTrucksByOrderCount(Pageable.ofSize(limit));
        return results.stream()
                .map(result -> {
                    FoodTruck ft = (FoodTruck) result[0];
                    Long orderCount = (Long) result[1];
                    return new FoodTruckDTO(ft.getId(), ft.getNombre(), ft.getTipoCocina(), ft.getUbicacionActual(), orderCount.intValue());
                })
                .collect(Collectors.toList());
    }

    public Double obtenerBeneficioMedioPorPedido(Long foodTruckId) {
        Optional<FoodTruck> foodTruckOpt = foodTruckRepository.findById(foodTruckId);
        if (!foodTruckOpt.isPresent()) {
            throw new IllegalArgumentException("FoodTruck no encontrado con ID: " + foodTruckId);
        }
        Double averageProfit = pedidoRepository.calculateAverageProfitByFoodTruck(foodTruckId);
        return averageProfit != null ? averageProfit : 0.0;
    }

    private FoodTruckDTO toDTO(FoodTruck foodTruck) {
        return new FoodTruckDTO(
                foodTruck.getId(),
                foodTruck.getNombre(),
                foodTruck.getTipoCocina(),
                foodTruck.getUbicacionActual(),
                0 // Default orderCount to 0 for non-top endpoints
        );
    }
}