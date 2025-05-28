import com.foodbites.truck_bites.dto.MenuDTO;
import com.foodbites.truck_bites.model.FoodTruck;
import com.foodbites.truck_bites.model.Menu;
import com.foodbites.truck_bites.repository.FoodTruckRepository;
import com.foodbites.truck_bites.repository.MenuRepository;
import com.foodbites.truck_bites.service.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @InjectMocks
    private MenuService menuService;

    private FoodTruck foodTruck;
    private Menu menu;
    private MenuDTO menuDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        foodTruck = new FoodTruck();
        foodTruck.setId(1L);
        foodTruck.setNombre("Taco Truck");

        menu = new Menu();
        menu.setId(1L);
        menu.setFoodTruck(foodTruck);
        menu.setNombre("Tacos");
        menu.setDescripcion("Delicious tacos");
        menu.setPrecio(5.99);
        menu.setImagenUrl("/img/tacos.jpg");

        menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        menuDTO.setFoodTruckId(1L);
        menuDTO.setFoodTruckNombre("Taco Truck");
        menuDTO.setNombre("Tacos");
        menuDTO.setDescripcion("Delicious tacos");
        menuDTO.setPrecio(5.99);
        menuDTO.setImagenUrl("/img/tacos.jpg");
    }

    @Test
    void testCrearMenu_Success() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        // Act
        MenuDTO result = menuService.crearMenu(menuDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getFoodTruckId());
        assertEquals("Tacos", result.getNombre());
        assertEquals("Delicious tacos", result.getDescripcion());
        assertEquals(5.99, result.getPrecio());
        assertEquals("/img/tacos.jpg", result.getImagenUrl());
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void testCrearMenu_NullNombre() {
        // Arrange
        menuDTO.setNombre(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.crearMenu(menuDTO);
        });
        assertEquals("El nombre del menú es obligatorio", exception.getMessage());
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testCrearMenu_EmptyNombre() {
        // Arrange
        menuDTO.setNombre(" ");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.crearMenu(menuDTO);
        });
        assertEquals("El nombre del menú es obligatorio", exception.getMessage());
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testCrearMenu_InvalidPrecio() {
        // Arrange
        menuDTO.setPrecio(0.0);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.crearMenu(menuDTO);
        });
        assertEquals("El precio debe ser mayor que 0", exception.getMessage());
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testCrearMenu_NullFoodTruckId() {
        // Arrange
        menuDTO.setFoodTruckId(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.crearMenu(menuDTO);
        });
        assertEquals("El ID del food truck es obligatorio", exception.getMessage());
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testCrearMenu_InvalidImagenUrl() {
        // Arrange
        menuDTO.setImagenUrl("invalid/path.jpg");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.crearMenu(menuDTO);
        });
        assertEquals("La URL de la imagen debe comenzar con /img/", exception.getMessage());
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testCrearMenu_FoodTruckNotFound() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.crearMenu(menuDTO);
        });
        assertEquals("Food Truck no encontrado con ID: 1", exception.getMessage());
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testCrearMenus_Success() {
        // Arrange
        List<MenuDTO> menuDTOs = Arrays.asList(menuDTO, menuDTO);
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        // Act
        List<MenuDTO> result = menuService.crearMenus(menuDTOs);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Tacos", result.get(0).getNombre());
        verify(foodTruckRepository, times(2)).findById(1L);
        verify(menuRepository, times(2)).save(any(Menu.class));
    }

    @Test
    void testCrearMenus_EmptyList() {
        // Arrange
        List<MenuDTO> menuDTOs = Collections.emptyList();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.crearMenus(menuDTOs);
        });
        assertEquals("La lista de menús no puede estar vacía", exception.getMessage());
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testObtenerMenus() {
        // Arrange
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu));

        // Act
        List<MenuDTO> result = menuService.obtenerMenus();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Tacos", result.get(0).getNombre());
        assertEquals(1L, result.get(0).getFoodTruckId());
        verify(menuRepository, times(1)).findAll();
    }

    @Test
    void testObtenerMenuPorId_Success() {
        // Arrange
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // Act
        MenuDTO result = menuService.obtenerMenuPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Tacos", result.getNombre());
        assertEquals(5.99, result.getPrecio());
        verify(menuRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerMenuPorId_NotFound() {
        // Arrange
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.obtenerMenuPorId(1L);
        });
        assertEquals("Menú no encontrado con ID: 1", exception.getMessage());
        verify(menuRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarMenu_Success() {
        // Arrange
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        MenuDTO updatedDTO = new MenuDTO();
        updatedDTO.setFoodTruckId(1L);
        updatedDTO.setNombre("Burritos");
        updatedDTO.setDescripcion("Tasty burritos");
        updatedDTO.setPrecio(6.99);
        updatedDTO.setImagenUrl("/img/burritos.jpg");

        // Act
        MenuDTO result = menuService.actualizarMenu(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Burritos", result.getNombre());
        assertEquals("Tasty burritos", result.getDescripcion());
        assertEquals(6.99, result.getPrecio());
        assertEquals("/img/burritos.jpg", result.getImagenUrl());
        verify(menuRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void testActualizarMenu_PartialUpdate() {
        // Arrange
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        MenuDTO partialDTO = new MenuDTO();
        partialDTO.setNombre("Updated Tacos");

        // Act
        MenuDTO result = menuService.actualizarMenu(1L, partialDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Tacos", result.getNombre());
        assertEquals("Delicious tacos", result.getDescripcion()); // Unchanged
        assertEquals(5.99, result.getPrecio()); // Unchanged
        verify(menuRepository, times(1)).findById(1L);
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void testActualizarMenu_NotFound() {
        // Arrange
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.actualizarMenu(1L, menuDTO);
        });
        assertEquals("Menú no encontrado con ID: 1", exception.getMessage());
        verify(menuRepository, times(1)).findById(1L);
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testActualizarMenu_FoodTruckNotFound() {
        // Arrange
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.empty());

        MenuDTO updatedDTO = new MenuDTO();
        updatedDTO.setFoodTruckId(1L);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.actualizarMenu(1L, updatedDTO);
        });
        assertEquals("Food Truck no encontrado con ID: 1", exception.getMessage());
        verify(menuRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testActualizarMenu_InvalidImagenUrl() {
        // Arrange
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        MenuDTO updatedDTO = new MenuDTO();
        updatedDTO.setImagenUrl("invalid/path.jpg");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.actualizarMenu(1L, updatedDTO);
        });
        assertEquals("La URL de la imagen debe comenzar con /img/", exception.getMessage());
        verify(menuRepository, times(1)).findById(1L);
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testEliminarMenu_Success() {
        // Arrange
        when(menuRepository.existsById(1L)).thenReturn(true);

        // Act
        menuService.eliminarMenu(1L);

        // Assert
        verify(menuRepository, times(1)).existsById(1L);
        verify(menuRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarMenu_NotFound() {
        // Arrange
        when(menuRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.eliminarMenu(1L);
        });
        assertEquals("Menú no encontrado con ID: 1", exception.getMessage());
        verify(menuRepository, times(1)).existsById(1L);
        verify(menuRepository, never()).deleteById(anyLong());
    }

    @Test
    void testObtenerMenusPorFoodTruck() {
        // Arrange
        when(menuRepository.findByFoodTruckId(1L)).thenReturn(Arrays.asList(menu));

        // Act
        List<MenuDTO> result = menuService.obtenerMenusPorFoodTruck(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getFoodTruckId());
        assertEquals("Tacos", result.get(0).getNombre());
        verify(menuRepository, times(1)).findByFoodTruckId(1L);
    }

    @Test
    void testObtenerMenusPorFoodTruck_NoMenus() {
        // Arrange
        when(menuRepository.findByFoodTruckId(1L)).thenReturn(Collections.emptyList());

        // Act
        List<MenuDTO> result = menuService.obtenerMenusPorFoodTruck(1L);

        // Assert
        assertEquals(0, result.size());
        verify(menuRepository, times(1)).findByFoodTruckId(1L);
    }
}