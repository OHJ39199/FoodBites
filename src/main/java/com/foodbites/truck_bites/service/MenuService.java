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

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuDTO crearMenu(MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setNombre(menuDTO.getNombre());
        menu.setDescripcion(menuDTO.getDescripcion());
        menu.setPrecio(menuDTO.getPrecio());
        menu = menuRepository.save(menu);
        return toDTO(menu);
    }

    public List<MenuDTO> obtenerMenus() {
        return menuRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MenuDTO obtenerMenuPorId(Long id) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isPresent()) {
            return toDTO(menu.get());
        }
        throw new IllegalArgumentException("Menú no encontrado con ID: " + id);
    }

    @Transactional
    public MenuDTO actualizarMenu(Long id, MenuDTO menuDTO) {
        Optional<Menu> menuOpt = menuRepository.findById(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setNombre(menuDTO.getNombre());
            menu.setDescripcion(menuDTO.getDescripcion());
            menu.setPrecio(menuDTO.getPrecio());
            menu = menuRepository.save(menu);
            return toDTO(menu);
        }
        throw new IllegalArgumentException("Menú no encontrado con ID: " + id);
    }

    @Transactional
    public void eliminarMenu(Long id) {
        if (menuRepository.existsById(id)) {
            menuRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Menú no encontrado con ID: " + id);
        }
    }

    public List<MenuDTO> obtenerMenusPorFoodTruck(Long foodTruckId) {
        return menuRepository.findByFoodTruckId(foodTruckId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MenuDTO toDTO(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setNombre(menu.getNombre());
        dto.setDescripcion(menu.getDescripcion());
        dto.setPrecio(menu.getPrecio());
        dto.setFoodTruckId(menu.getFoodTruck().getId());
        dto.setFoodTruckNombre(menu.getFoodTruck().getNombre());
        return dto;
    }
}