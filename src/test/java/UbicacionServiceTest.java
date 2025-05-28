import com.foodbites.truck_bites.dto.UbicacionDTO;
import com.foodbites.truck_bites.model.Ubicacion;
import com.foodbites.truck_bites.model.FoodTruck;
import com.foodbites.truck_bites.repository.UbicacionRepository;
import com.foodbites.truck_bites.repository.FoodTruckRepository;
import com.foodbites.truck_bites.service.UbicacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UbicacionServiceTest {

    @Mock
    private UbicacionRepository ubicacionRepository;

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @InjectMocks
    private UbicacionService ubicacionService;

    private FoodTruck foodTruck;
    private Ubicacion ubicacion;
    private UbicacionDTO ubicacionDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        foodTruck = new FoodTruck();
        foodTruck.setId(1L);
        foodTruck.setNombre("Test Truck");

        ubicacion = new Ubicacion();
        ubicacion.setId(1L);
        ubicacion.setFoodTruck(foodTruck);
        ubicacion.setCoordenadas("40.7128,-74.0060");
        ubicacion.setFechaRegistro(LocalDateTime.now());

        ubicacionDTO = new UbicacionDTO();
        ubicacionDTO.setFoodTruckId(1L);
        ubicacionDTO.setCoordenadas("40.7128,-74.0060");
    }

    @Test
    void testCrearUbicacion_Success() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(ubicacion);

        // Act
        UbicacionDTO result = ubicacionService.crearUbicacion(ubicacionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getFoodTruckId());
        assertEquals("40.7128,-74.0060", result.getCoordenadas());
        assertNotNull(result.getFechaRegistro());
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(ubicacionRepository, times(1)).save(any(Ubicacion.class));
    }

    @Test
    void testCrearUbicacion_FoodTruckNotFound() {
        // Arrange
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ubicacionService.crearUbicacion(ubicacionDTO);
        });
        assertEquals("FoodTruck no encontrado con ID: 1", exception.getMessage());
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(ubicacionRepository, never()).save(any(Ubicacion.class));
    }

    @Test
    void testObtenerUbicaciones() {
        // Arrange
        when(ubicacionRepository.findAll()).thenReturn(Arrays.asList(ubicacion));

        // Act
        List<UbicacionDTO> result = ubicacionService.obtenerUbicaciones();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getFoodTruckId());
        assertEquals("40.7128,-74.0060", result.get(0).getCoordenadas());
        assertNotNull(result.get(0).getFechaRegistro());
        verify(ubicacionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerUbicacionPorId_Success() {
        // Arrange
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));

        // Act
        UbicacionDTO result = ubicacionService.obtenerUbicacionPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getFoodTruckId());
        assertEquals("40.7128,-74.0060", result.getCoordenadas());
        assertNotNull(result.getFechaRegistro());
        verify(ubicacionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerUbicacionPorId_NotFound() {
        // Arrange
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ubicacionService.obtenerUbicacionPorId(1L);
        });
        assertEquals("Ubicación no encontrada con ID: 1", exception.getMessage());
        verify(ubicacionRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarUbicacion_Success() {
        // Arrange
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(ubicacion);

        // Act
        UbicacionDTO result = ubicacionService.actualizarUbicacion(1L, ubicacionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getFoodTruckId());
        assertEquals("40.7128,-74.0060", result.getCoordenadas());
        assertNotNull(result.getFechaRegistro());
        verify(ubicacionRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(ubicacionRepository, times(1)).save(any(Ubicacion.class));
    }

    @Test
    void testActualizarUbicacion_UbicacionNotFound() {
        // Arrange
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ubicacionService.actualizarUbicacion(1L, ubicacionDTO);
        });
        assertEquals("Ubicación no encontrada con ID: 1", exception.getMessage());
        verify(ubicacionRepository, times(1)).findById(1L);
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(ubicacionRepository, never()).save(any(Ubicacion.class));
    }

    @Test
    void testActualizarUbicacion_FoodTruckNotFound() {
        // Arrange
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ubicacionService.actualizarUbicacion(1L, ubicacionDTO);
        });
        assertEquals("FoodTruck no encontrado con ID: 1", exception.getMessage());
        verify(ubicacionRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(ubicacionRepository, never()).save(any(Ubicacion.class));
    }

    @Test
    void testEliminarUbicacion_Success() {
        // Arrange
        when(ubicacionRepository.existsById(1L)).thenReturn(true);

        // Act
        ubicacionService.eliminarUbicacion(1L);

        // Assert
        verify(ubicacionRepository, times(1)).existsById(1L);
        verify(ubicacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarUbicacion_NotFound() {
        // Arrange
        when(ubicacionRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ubicacionService.eliminarUbicacion(1L);
        });
        assertEquals("Ubicación no encontrada con ID: 1", exception.getMessage());
        verify(ubicacionRepository, times(1)).existsById(1L);
        verify(ubicacionRepository, never()).deleteById(anyLong());
    }

    @Test
    void testObtenerUbicacionesRecientes() {
        // Arrange
        when(ubicacionRepository.findByFoodTruckIdOrderByFechaRegistroDesc(1L)).thenReturn(Arrays.asList(ubicacion));

        // Act
        List<UbicacionDTO> result = ubicacionService.obtenerUbicacionesRecientes(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getFoodTruckId());
        assertEquals("40.7128,-74.0060", result.get(0).getCoordenadas());
        assertNotNull(result.get(0).getFechaRegistro());
        verify(ubicacionRepository, times(1)).findByFoodTruckIdOrderByFechaRegistroDesc(1L);
    }
}