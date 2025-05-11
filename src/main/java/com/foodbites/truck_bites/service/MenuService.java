package com.foodbites.truck_bites.service;

import com.foodbites.truck_bites.dto.MenuDTO;
import com.foodbites.truck_bites.model.Menu;
import com.foodbites.truck_bites.model.FoodTruck;
import com.foodbites.truck_bites.repository.MenuRepository;
import com.foodbites.truck_bites.repository.FoodTruckRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con menús.
 */
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final FoodTruckRepository foodTruckRepository;

    /**
     * Constructor del servicio de menús.
     *
     * @param menuRepository       Repositorio de menús.
     * @param foodTruckRepository  Repositorio de food trucks.
     */
    public MenuService(MenuRepository menuRepository, FoodTruckRepository foodTruckRepository) {
        this.menuRepository = menuRepository;
        this.foodTruckRepository = foodTruckRepository;
    }

    /**
     * Crea un nuevo menú.
     *
     * @param menuDTO DTO del menú a crear.
     * @return El DTO del menú creado.
     */
    @Transactional
    public MenuDTO crearMenu(MenuDTO menuDTO) {
        // Validar inputs
        if (menuDTO.getNombre() == null || menuDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del menú es obligatorio");
        }
        if (menuDTO.getPrecio() == null || menuDTO.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0");
        }
        if (menuDTO.getFoodTruckId() == null) {
            throw new IllegalArgumentException("El ID del food truck es obligatorio");
        }
        if (menuDTO.getImagenUrl() != null && !menuDTO.getImagenUrl().startsWith("/img/")) {
            throw new IllegalArgumentException("La URL de la imagen debe comenzar con /img/");
        }

        // Validar food truck existe
        FoodTruck foodTruck = foodTruckRepository.findById(menuDTO.getFoodTruckId())
                .orElseThrow(() -> new IllegalArgumentException("Food Truck no encontrado con ID: " + menuDTO.getFoodTruckId()));

        Menu menu = new Menu();
        menu.setFoodTruck(foodTruck);
        menu.setNombre(menuDTO.getNombre());
        menu.setDescripcion(menuDTO.getDescripcion());
        menu.setPrecio(menuDTO.getPrecio());
        menu.setImagenUrl(menuDTO.getImagenUrl());
        menu = menuRepository.save(menu);
        return toDTO(menu);
    }

    /**
     * Crea una lista de menús.
     *
     * @param menuDTOs Lista de DTOs de menús a crear.
     * @return Lista de DTOs de menús creados.
     */
    @Transactional
    public List<MenuDTO> crearMenus(List<MenuDTO> menuDTOs) {
        if (menuDTOs == null || menuDTOs.isEmpty()) {
            throw new IllegalArgumentException("La lista de menús no puede estar vacía");
        }
        return menuDTOs.stream()
                .map(this::crearMenu)
                .collect(Collectors.toList());
    }
    /**
     * Obtiene todos los menús.
     *
     * @return Lista de DTOs de menús.
     */
    public List<MenuDTO> obtenerMenus() {
        return menuRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    /**
     * Obtiene un menú por su ID.
     *
     * @param id ID del menú a obtener.
     * @return El DTO del menú.
     */
    public MenuDTO obtenerMenuPorId(Long id) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isPresent()) {
            return toDTO(menu.get());
        }
        throw new IllegalArgumentException("Menú no encontrado con ID: " + id);
    }
    /**
     * Actualiza un menú.
     *
     * @param id      ID del menú a actualizar.
     * @param menuDTO DTO con los nuevos datos del menú.
     * @return El DTO del menú actualizado.
     */
    @Transactional
    public MenuDTO actualizarMenu(Long id, MenuDTO menuDTO) {
        Optional<Menu> menuOpt = menuRepository.findById(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            if (menuDTO.getFoodTruckId() != null) {
                FoodTruck foodTruck = foodTruckRepository.findById(menuDTO.getFoodTruckId())
                        .orElseThrow(() -> new IllegalArgumentException("Food Truck no encontrado con ID: " + menuDTO.getFoodTruckId()));
                menu.setFoodTruck(foodTruck);
            }
            if (menuDTO.getNombre() != null) {
                menu.setNombre(menuDTO.getNombre());
            }
            if (menuDTO.getDescripcion() != null) {
                menu.setDescripcion(menuDTO.getDescripcion());
            }
            if (menuDTO.getPrecio() != null) {
                menu.setPrecio(menuDTO.getPrecio());
            }
            if (menuDTO.getImagenUrl() != null && !menuDTO.getImagenUrl().startsWith("/img/")) {
                throw new IllegalArgumentException("La URL de la imagen debe comenzar con /img/");
            }
            if (menuDTO.getImagenUrl() != null) {
                menu.setImagenUrl(menuDTO.getImagenUrl());
            }
            menu = menuRepository.save(menu);
            return toDTO(menu);
        }
        throw new IllegalArgumentException("Menú no encontrado con ID: " + id);
    }
    /**
     * Elimina un menú por su ID.
     *
     * @param id ID del menú a eliminar.
     */
    @Transactional
    public void eliminarMenu(Long id) {
        if (menuRepository.existsById(id)) {
            menuRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Menú no encontrado con ID: " + id);
        }
    }
    /**
     * Obtiene los menús de un food truck específico.
     *
     * @param foodTruckId ID del food truck.
     * @return Lista de DTOs de menús.
     */
    public List<MenuDTO> obtenerMenusPorFoodTruck(Long foodTruckId) {
        return menuRepository.findByFoodTruckId(foodTruckId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    /**
     * Convierte un objeto Menu a su correspondiente DTO.
     *
     * @param menu Objeto Menu a convertir.
     * @return El DTO correspondiente al menú.
     */
    private MenuDTO toDTO(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setFoodTruckId(menu.getFoodTruck().getId());
        dto.setFoodTruckNombre(menu.getFoodTruck().getNombre());
        dto.setNombre(menu.getNombre());
        dto.setDescripcion(menu.getDescripcion());
        dto.setPrecio(menu.getPrecio());
        dto.setImagenUrl(menu.getImagenUrl());
        return dto;
    }
}