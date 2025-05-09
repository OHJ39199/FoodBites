package com.foodbites.truck_bites.service;

import com.foodbites.truck_bites.dto.UbicacionDTO;
import com.foodbites.truck_bites.model.Ubicacion;
import com.foodbites.truck_bites.model.FoodTruck;
import com.foodbites.truck_bites.repository.UbicacionRepository;
import com.foodbites.truck_bites.repository.FoodTruckRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con ubicaciones.
 */
@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final FoodTruckRepository foodTruckRepository;

    public UbicacionService(UbicacionRepository ubicacionRepository, FoodTruckRepository foodTruckRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.foodTruckRepository = foodTruckRepository;
    }

    @Transactional
    public UbicacionDTO crearUbicacion(UbicacionDTO ubicacionDTO) {
        Optional<FoodTruck> foodTruck = foodTruckRepository.findById(ubicacionDTO.getFoodTruckId());
        if (foodTruck.isPresent()) {
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setFoodTruck(foodTruck.get());
            ubicacion.setCoordenadas(ubicacionDTO.getCoordenadas());
            ubicacion = ubicacionRepository.save(ubicacion);
            return toDTO(ubicacion);
        }
        throw new IllegalArgumentException("FoodTruck no encontrado con ID: " + ubicacionDTO.getFoodTruckId());
    }

    public List<UbicacionDTO> obtenerUbicaciones() {
        return ubicacionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UbicacionDTO obtenerUbicacionPorId(Long id) {
        Optional<Ubicacion> ubicacion = ubicacionRepository.findById(id);
        if (ubicacion.isPresent()) {
            return toDTO(ubicacion.get());
        }
        throw new IllegalArgumentException("Ubicación no encontrada con ID: " + id);
    }

    @Transactional
    public UbicacionDTO actualizarUbicacion(Long id, UbicacionDTO ubicacionDTO) {
        Optional<Ubicacion> ubicacionOpt = ubicacionRepository.findById(id);
        if (ubicacionOpt.isPresent()) {
            Ubicacion ubicacion = ubicacionOpt.get();
            Optional<FoodTruck> foodTruck = foodTruckRepository.findById(ubicacionDTO.getFoodTruckId());
            if (foodTruck.isPresent()) {
                ubicacion.setFoodTruck(foodTruck.get());
                ubicacion.setCoordenadas(ubicacionDTO.getCoordenadas());
                ubicacion = ubicacionRepository.save(ubicacion);
                return toDTO(ubicacion);
            }
            throw new IllegalArgumentException("FoodTruck no encontrado con ID: " + ubicacionDTO.getFoodTruckId());
        }
        throw new IllegalArgumentException("Ubicación no encontrada con ID: " + id);
    }

    @Transactional
    public void eliminarUbicacion(Long id) {
        if (ubicacionRepository.existsById(id)) {
            ubicacionRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Ubicación no encontrada con ID: " + id);
        }
    }

    public List<UbicacionDTO> obtenerUbicacionesRecientes(Long foodTruckId) {
        return ubicacionRepository.findByFoodTruckIdOrderByFechaRegistroDesc(foodTruckId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private UbicacionDTO toDTO(Ubicacion ubicacion) {
        UbicacionDTO dto = new UbicacionDTO();
        dto.setId(ubicacion.getId());
        dto.setFoodTruckId(ubicacion.getFoodTruck().getId());
        dto.setCoordenadas(ubicacion.getCoordenadas());
        dto.setFechaRegistro(ubicacion.getFechaRegistro());
        return dto;
    }
}
