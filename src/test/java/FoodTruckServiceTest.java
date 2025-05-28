import com.foodbites.truck_bites.dto.FoodTruckDTO;
import com.foodbites.truck_bites.model.FoodTruck;
import com.foodbites.truck_bites.repository.FoodTruckRepository;
import com.foodbites.truck_bites.repository.PedidoRepository;
import com.foodbites.truck_bites.service.FoodTruckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodTruckServiceTest {

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private FoodTruckService foodTruckService;

    private FoodTruck foodTruck;
    private FoodTruckDTO foodTruckDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        foodTruck = new FoodTruck();
        foodTruck.setId(1L);
        foodTruck.setNombre("Taco Truck");
        foodTruck.setTipoCocina("Mexican");
        foodTruck.setUbicacionActual("Madrid, Calle Gran Vía");

        foodTruckDTO = new FoodTruckDTO();
        foodTruckDTO.setId(1L);
        foodTruckDTO.setNombre("Taco Truck");
        foodTruckDTO.setTipoCocina("Mexican");
        foodTruckDTO.setUbicacionActual("Madrid, Calle Gran Vía");
        foodTruckDTO.setOrderCount(0);
    }

    @Test
    void testObtenerMenusMasConsumidos_EmptyResult() {
        // Arrange
        when(pedidoRepository.findMostConsumedMenuItemsForAllFoodTrucks()).thenReturn(Arrays.asList());

        // Act
        List<Map<String, Object>> resultList = foodTruckService.obtenerMenusMasConsumidos();

        // Assert
        assertEquals(0, resultList.size());
        verify(pedidoRepository, times(1)).findMostConsumedMenuItemsForAllFoodTrucks();
    }

    @Test
    void testCrearFoodTruck_Success() {
        // Arrange
        when(foodTruckRepository.save(any(FoodTruck.class))).thenReturn(foodTruck);

        // Act
        FoodTruckDTO result = foodTruckService.crearFoodTruck(foodTruckDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Taco Truck", result.getNombre());
        assertEquals("Mexican", result.getTipoCocina());
        assertEquals("Madrid, Calle Gran Vía", result.getUbicacionActual());
        assertEquals(0, result.getOrderCount());
        verify(foodTruckRepository, times(1)).save(any(FoodTruck.class));
    }

    @Test
    void testObtenerFoodTrucks() {
        // Arrange
        when(foodTruckRepository.findAll()).thenReturn(Arrays.asList(foodTruck));

        // Act
        List<FoodTruckDTO> result = foodTruckService.obtenerFoodTrucks();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Taco Truck", result.get(0).getNombre());
        assertEquals("Mexican", result.get(0).getTipoCocina());
        assertEquals("Madrid, Calle Gran Vía", result.get(0).getUbicacionActual());
        verify(foodTruckRepository, times(1)).findAll();
    }

    @Test
    void testObtenerFoodTruckPorId_Success() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));

        // Act
        FoodTruckDTO result = foodTruckService.obtenerFoodTruckPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Taco Truck", result.getNombre());
        assertEquals("Mexican", result.getTipoCocina());
        verify(foodTruckRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerFoodTruckPorId_NotFound() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            foodTruckService.obtenerFoodTruckPorId(1L);
        });
        assertEquals("FoodTruck no encontrado con ID: 1", exception.getMessage());
        verify(foodTruckRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarFoodTruck_Success() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(foodTruckRepository.save(any(FoodTruck.class))).thenReturn(foodTruck);

        FoodTruckDTO updatedDTO = new FoodTruckDTO();
        updatedDTO.setNombre("Updated Truck");
        updatedDTO.setTipoCocina("Italian");
        updatedDTO.setUbicacionActual("Barcelona, Ramblas");

        // Act
        FoodTruckDTO result = foodTruckService.actualizarFoodTruck(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Truck", result.getNombre());
        assertEquals("Italian", result.getTipoCocina());
        assertEquals("Barcelona, Ramblas", result.getUbicacionActual());
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).save(any(FoodTruck.class));
    }

    @Test
    void testActualizarFoodTruck_PartialUpdate() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(foodTruckRepository.save(any(FoodTruck.class))).thenReturn(foodTruck);

        FoodTruckDTO partialDTO = new FoodTruckDTO();
        partialDTO.setNombre("Updated Truck");

        // Act
        FoodTruckDTO result = foodTruckService.actualizarFoodTruck(1L, partialDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Truck", result.getNombre());
        assertEquals("Mexican", result.getTipoCocina());
        assertEquals("Madrid, Calle Gran Vía", result.getUbicacionActual());
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).save(any(FoodTruck.class));
    }

    @Test
    void testActualizarFoodTruck_NotFound() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            foodTruckService.actualizarFoodTruck(1L, foodTruckDTO);
        });
        assertEquals("FoodTruck no encontrado con ID: 1", exception.getMessage());
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(foodTruckRepository, never()).save(any(FoodTruck.class));
    }

    @Test
    void testEliminarFoodTruck_Success() {
        // Arrange
        when(foodTruckRepository.existsById(1L)).thenReturn(true);

        // Act
        foodTruckService.eliminarFoodTruck(1L);

        // Assert
        verify(foodTruckRepository, times(1)).existsById(1L);
        verify(foodTruckRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarFoodTruck_NotFound() {
        // Arrange
        when(foodTruckRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            foodTruckService.eliminarFoodTruck(1L);
        });
        assertEquals("FoodTruck no encontrado con ID: 1", exception.getMessage());
        verify(foodTruckRepository, times(1)).existsById(1L);
        verify(foodTruckRepository, never()).deleteById(anyLong());
    }

    @Test
    void testObtenerFoodTrucksCerca_CityOnly() {
        // Arrange
        when(foodTruckRepository.findByUbicacionActualContainingIgnoreCase("Madrid"))
                .thenReturn(Arrays.asList(foodTruck));

        // Act
        List<FoodTruckDTO> result = foodTruckService.obtenerFoodTrucksCerca("Madrid", null);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Taco Truck", result.get(0).getNombre());
        verify(foodTruckRepository, times(1)).findByUbicacionActualContainingIgnoreCase("Madrid");
    }

    @Test
    void testObtenerFoodTrucksCerca_CityAndStreet() {
        // Arrange
        when(foodTruckRepository.findByUbicacionActualContainingIgnoreCase("Madrid"))
                .thenReturn(Arrays.asList(foodTruck));

        // Act
        List<FoodTruckDTO> result = foodTruckService.obtenerFoodTrucksCerca("Madrid", "Calle Gran Vía");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Taco Truck", result.get(0).getNombre());
        verify(foodTruckRepository, times(1)).findByUbicacionActualContainingIgnoreCase("Madrid");
    }

    @Test
    void testObtenerFoodTrucksCerca_NoMatches() {
        // Arrange
        when(foodTruckRepository.findByUbicacionActualContainingIgnoreCase("Madrid"))
                .thenReturn(Arrays.asList());

        // Act
        List<FoodTruckDTO> result = foodTruckService.obtenerFoodTrucksCerca("Madrid", "Calle No Existe");

        // Assert
        assertEquals(0, result.size());
        verify(foodTruckRepository, times(1)).findByUbicacionActualContainingIgnoreCase("Madrid");
    }

    @Test
    void testRecomendarFoodTrucks() {
        // Arrange
        when(foodTruckRepository.findByUbicacionActualContainingIgnoreCase("Madrid"))
                .thenReturn(Arrays.asList(foodTruck));

        // Act
        List<FoodTruckDTO> result = foodTruckService.recomendarFoodTrucks("Madrid", "Mexican");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Taco Truck", result.get(0).getNombre());
        verify(foodTruckRepository, times(1)).findByUbicacionActualContainingIgnoreCase("Madrid");
    }

    @Test
    void testRecomendarFoodTrucks_NoMatches() {
        // Arrange
        when(foodTruckRepository.findByUbicacionActualContainingIgnoreCase("Madrid"))
                .thenReturn(Arrays.asList(foodTruck));

        // Act
        List<FoodTruckDTO> result = foodTruckService.recomendarFoodTrucks("Madrid", "Italian");

        // Assert
        assertEquals(0, result.size());
        verify(foodTruckRepository, times(1)).findByUbicacionActualContainingIgnoreCase("Madrid");
    }

    @Test
    void testObtenerTopFoodTrucksPorPedidos_InvalidLimit() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            foodTruckService.obtenerTopFoodTrucksPorPedidos(0);
        });
        assertEquals("El límite debe ser mayor que 0", exception.getMessage());
        verify(pedidoRepository, never()).findTopFoodTrucksByOrderCount(any(Pageable.class));
    }

    @Test
    void testObtenerBeneficioMedioPorPedido_Success() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(pedidoRepository.calculateAverageProfitByFoodTruck(1L)).thenReturn(50.0);

        // Act
        Double result = foodTruckService.obtenerBeneficioMedioPorPedido(1L);

        // Assert
        assertEquals(50.0, result);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).calculateAverageProfitByFoodTruck(1L);
    }

    @Test
    void testObtenerBeneficioMedioPorPedido_NoProfit() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(pedidoRepository.calculateAverageProfitByFoodTruck(1L)).thenReturn(null);

        // Act
        Double result = foodTruckService.obtenerBeneficioMedioPorPedido(1L);

        // Assert
        assertEquals(0.0, result);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).calculateAverageProfitByFoodTruck(1L);
    }

    @Test
    void testObtenerBeneficioMedioPorPedido_FoodTruckNotFound() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            foodTruckService.obtenerBeneficioMedioPorPedido(1L);
        });
        assertEquals("FoodTruck no encontrado con ID: 1", exception.getMessage());
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).calculateAverageProfitByFoodTruck(anyLong());
    }
}
