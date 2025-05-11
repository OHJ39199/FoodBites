package com.foodbites.truck_bites.controller;

import com.foodbites.truck_bites.dto.MenuDTO;
import com.foodbites.truck_bites.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
/**
 * Controlador REST para gestionar operaciones relacionadas con menús.
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;
    private static final String UPLOAD_DIR = "src/main/resources/static/img/";

    /**
     * Constructor del controlador de menús.
     *
     * @param menuService Servicio de menús.
     */
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    /**
     * Crea un nuevo menú.
     *
     * @param menuDTO DTO del menú a crear.
     * @return El DTO del menú creado.
     */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<MenuDTO> crearMenu(
            @RequestPart("menu") MenuDTO menuDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            image.transferTo(new File(UPLOAD_DIR + fileName));
            menuDTO.setImagenUrl("/img/" + fileName);
        }
        return ResponseEntity.ok(menuService.crearMenu(menuDTO));
    }
    /**
     * Crea múltiples menús.
     *
     * @param menuDTOs Lista de DTOs de menús a crear.
     * @return Lista de DTOs de menús creados.
     */
    @PostMapping(value = "/bulk", consumes = {"multipart/form-data"})
    public ResponseEntity<List<MenuDTO>> crearMenus(
            @RequestPart("menus") List<MenuDTO> menuDTOs,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        if (images != null && !images.isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            for (int i = 0; i < Math.min(menuDTOs.size(), images.size()); i++) {
                MultipartFile image = images.get(i);
                if (!image.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                    image.transferTo(new File(UPLOAD_DIR + fileName));
                    menuDTOs.get(i).setImagenUrl("/img/" + fileName);
                }
            }
        }
        return ResponseEntity.ok(menuService.crearMenus(menuDTOs));
    }
    /**
     * Obtiene todos los menús.
     *
     * @return Lista de DTOs de menús.
     */
    @GetMapping
    public ResponseEntity<List<MenuDTO>> obtenerMenus() {
        return ResponseEntity.ok(menuService.obtenerMenus());
    }
    /**
     * Obtiene un menú por su ID.
     *
     * @param id ID del menú a obtener.
     * @return El DTO del menú encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> obtenerMenuPorId(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.obtenerMenuPorId(id));
    }
    /**
     * Actualiza un menú.
     *
     * @param id      ID del menú a actualizar.
     * @param menuDTO DTO con los nuevos datos del menú.
     * @return El DTO del menú actualizado.
     */
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<MenuDTO> actualizarMenu(
            @PathVariable Long id,
            @RequestPart("menu") MenuDTO menuDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            image.transferTo(new File(UPLOAD_DIR + fileName));
            menuDTO.setImagenUrl("/img/" + fileName);
        }
        return ResponseEntity.ok(menuService.actualizarMenu(id, menuDTO));
    }
    /**
     * Elimina un menú por su ID.
     *
     * @param id ID del menú a eliminar.
     * @return Respuesta vacía con estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMenu(@PathVariable Long id) {
        menuService.eliminarMenu(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Obtiene todos los menús de un food truck específico.
     *
     * @param foodTruckId ID del food truck.
     * @return Lista de DTOs de menús del food truck.
     */
    @GetMapping("/foodtruck/{foodTruckId}")
    public ResponseEntity<List<MenuDTO>> obtenerMenusPorFoodTruck(@PathVariable Long foodTruckId) {
        return ResponseEntity.ok(menuService.obtenerMenusPorFoodTruck(foodTruckId));
    }
}
