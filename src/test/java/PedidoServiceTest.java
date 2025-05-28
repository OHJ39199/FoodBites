import com.foodbites.truck_bites.dto.PedidoDTO;
import com.foodbites.truck_bites.model.FoodTruck;
import com.foodbites.truck_bites.model.Pedido;
import com.foodbites.truck_bites.model.Usuario;
import com.foodbites.truck_bites.repository.FoodTruckRepository;
import com.foodbites.truck_bites.repository.PedidoRepository;
import com.foodbites.truck_bites.repository.UsuarioRepository;
import com.foodbites.truck_bites.service.PedidoService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private PedidoDTO pedidoDTO;
    private Usuario usuario;
    private FoodTruck foodTruck;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize test data
        now = LocalDateTime.now();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("John Doe");

        foodTruck = new FoodTruck();
        foodTruck.setId(1L);
        foodTruck.setNombre("Taco Truck");

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setUsuario(usuario);
        pedido.setFoodTruck(foodTruck);
        pedido.setItems("Tacos, Burritos");
        pedido.setMontoTotal(20.5);
        pedido.setEstado("PENDIENTE");
        pedido.setFechaCreacion(now);
        pedido.setFechaProgramada(now.plusHours(1));

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        pedidoDTO.setUsuarioId(1L);
        pedidoDTO.setFoodTruckId(1L);
        pedidoDTO.setFoodTruckNombre("Taco Truck");
        pedidoDTO.setItems("Tacos, Burritos");
        pedidoDTO.setMontoTotal(20.5);
        pedidoDTO.setEstado("PENDIENTE");
        pedidoDTO.setFechaCreacion(now);
        pedidoDTO.setFechaProgramada(now.plusHours(1));
    }

    @Test
    void testCrearPedido_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        PedidoDTO result = pedidoService.crearPedido(pedidoDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getUsuarioId());
        assertEquals(1L, result.getFoodTruckId());
        assertEquals("Taco Truck", result.getFoodTruckNombre());
        assertEquals("Tacos, Burritos", result.getItems());
        assertEquals(20.5, result.getMontoTotal());
        assertEquals("PENDIENTE", result.getEstado());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testCrearPedido_UsuarioNotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.crearPedido(pedidoDTO);
        });
        assertEquals("Usuario o FoodTruck no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testCrearPedido_FoodTruckNotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.crearPedido(pedidoDTO);
        });
        assertEquals("Usuario o FoodTruck no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testObtenerPedidos() {
        // Arrange
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedido));

        // Act
        List<PedidoDTO> result = pedidoService.obtenerPedidos();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        assertEquals(1L, result.get(0).getFoodTruckId());
        assertEquals("Tacos, Burritos", result.get(0).getItems());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPedidoPorId_Success() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act
        PedidoDTO result = pedidoService.obtenerPedidoPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUsuarioId());
        assertEquals(1L, result.getFoodTruckId());
        assertEquals("Taco Truck", result.getFoodTruckNombre());
        assertEquals(20.5, result.getMontoTotal());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPedidoPorId_NotFound() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.obtenerPedidoPorId(1L);
        });
        assertEquals("Pedido no encontrado con ID: 1", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarPedido_Success() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        PedidoDTO updatedDTO = new PedidoDTO();
        updatedDTO.setUsuarioId(1L);
        updatedDTO.setFoodTruckId(1L);
        updatedDTO.setItems("Quesadillas");
        updatedDTO.setMontoTotal(15.0);
        updatedDTO.setEstado("COMPLETADO");
        updatedDTO.setFechaProgramada(now.plusHours(2));

        PedidoDTO result = pedidoService.actualizarPedido(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Quesadillas", result.getItems());
        assertEquals(15.0, result.getMontoTotal());
        assertEquals("COMPLETADO", result.getEstado());
        verify(pedidoRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testActualizarPedido_PedidoNotFound() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.actualizarPedido(1L, pedidoDTO);
        });
        assertEquals("Pedido no encontrado con ID: 1", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).findById(anyLong());
        verify(foodTruckRepository, never()).findById(anyLong());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testActualizarPedido_UsuarioNotFound() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        when(foodTruckRepository.findById(1L)).thenReturn(Optional.of(foodTruck));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.actualizarPedido(1L, pedidoDTO);
        });
        assertEquals("Usuario o FoodTruck no encontrado", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(foodTruckRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testEliminarPedido_Success() {
        // Arrange
        when(pedidoRepository.existsById(1L)).thenReturn(true);

        // Act
        pedidoService.eliminarPedido(1L);

        // Assert
        verify(pedidoRepository, times(1)).existsById(1L);
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarPedido_NotFound() {
        // Arrange
        when(pedidoRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.eliminarPedido(1L);
        });
        assertEquals("Pedido no encontrado con ID: 1", exception.getMessage());
        verify(pedidoRepository, times(1)).existsById(1L);
        verify(pedidoRepository, never()).deleteById(anyLong());
    }

    @Test
    void testObtenerPedidosPendientes() {
        // Arrange
        when(pedidoRepository.findPendingByFoodTruckId(1L)).thenReturn(Arrays.asList(pedido));

        // Act
        List<PedidoDTO> result = pedidoService.obtenerPedidosPendientes(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getFoodTruckId());
        assertEquals("PENDIENTE", result.get(0).getEstado());
        verify(pedidoRepository, times(1)).findPendingByFoodTruckId(1L);
    }

    @Test
    void testObtenerVentasTotales_Success() {
        // Arrange
        LocalDateTime startDate = now.minusDays(7);
        LocalDateTime endDate = now;
        when(pedidoRepository.calculateTotalSalesByFoodTruck(1L, startDate, endDate)).thenReturn(100.0);

        // Act
        Double result = pedidoService.obtenerVentasTotales(1L, startDate, endDate);

        // Assert
        assertEquals(100.0, result);
        verify(pedidoRepository, times(1)).calculateTotalSalesByFoodTruck(1L, startDate, endDate);
    }

    @Test
    void testObtenerVentasTotales_NoSales() {
        // Arrange
        LocalDateTime startDate = now.minusDays(7);
        LocalDateTime endDate = now;
        when(pedidoRepository.calculateTotalSalesByFoodTruck(1L, startDate, endDate)).thenReturn(null);

        // Act
        Double result = pedidoService.obtenerVentasTotales(1L, startDate, endDate);

        // Assert
        assertEquals(0.0, result);
        verify(pedidoRepository, times(1)).calculateTotalSalesByFoodTruck(1L, startDate, endDate);
    }

    @Test
    void testObtenerPedidosPorUsuarioYFoodTruck() {
        // Arrange
        when(pedidoRepository.findByUsuarioIdAndFoodTruckId(1L, 1L)).thenReturn(Arrays.asList(pedido));

        // Act
        List<PedidoDTO> result = pedidoService.obtenerPedidosPorUsuarioYFoodTruck(1L, 1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        assertEquals(1L, result.get(0).getFoodTruckId());
        verify(pedidoRepository, times(1)).findByUsuarioIdAndFoodTruckId(1L, 1L);
    }

    @Test
    void testObtenerPedidosPorUsuario() {
        // Arrange
        when(pedidoRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(pedido));

        // Act
        List<PedidoDTO> result = pedidoService.obtenerPedidosPorUsuario(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        verify(pedidoRepository, times(1)).findByUsuarioId(1L);
    }
}
