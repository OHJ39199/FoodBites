import com.foodbites.truck_bites.dto.NotificacionDTO;
import com.foodbites.truck_bites.model.Notificacion;
import com.foodbites.truck_bites.model.Usuario;
import com.foodbites.truck_bites.repository.NotificacionRepository;
import com.foodbites.truck_bites.repository.UsuarioRepository;
import com.foodbites.truck_bites.service.NotificacionService;
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
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacion;
    private NotificacionDTO notificacionDTO;
    private Usuario usuario;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize test data
        now = LocalDateTime.now();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("John Doe");

        notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setUsuario(usuario);
        notificacion.setMensaje("Tu pedido está listo!");
        notificacion.setFechaEnvio(now);

        notificacionDTO = new NotificacionDTO();
        notificacionDTO.setId(1L);
        notificacionDTO.setUsuarioId(1L);
        notificacionDTO.setMensaje("Tu pedido está listo!");
        notificacionDTO.setFechaEnvio(now);
    }

    @Test
    void testCrearNotificacion_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        // Act
        NotificacionDTO result = notificacionService.crearNotificacion(notificacionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getUsuarioId());
        assertEquals("Tu pedido está listo!", result.getMensaje());
        assertEquals(now, result.getFechaEnvio());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void testCrearNotificacion_UsuarioNotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificacionService.crearNotificacion(notificacionDTO);
        });
        assertEquals("Usuario no encontrado con ID: 1", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    void testObtenerNotificaciones() {
        // Arrange
        when(notificacionRepository.findAll()).thenReturn(Arrays.asList(notificacion));

        // Act
        List<NotificacionDTO> result = notificacionService.obtenerNotificaciones();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        assertEquals("Tu pedido está listo!", result.get(0).getMensaje());
        verify(notificacionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerNotificacionPorId_Success() {
        // Arrange
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        // Act
        NotificacionDTO result = notificacionService.obtenerNotificacionPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUsuarioId());
        assertEquals("Tu pedido está listo!", result.getMensaje());
        verify(notificacionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerNotificacionPorId_NotFound() {
        // Arrange
        when(notificacionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificacionService.obtenerNotificacionPorId(1L);
        });
        assertEquals("Notificación no encontrada con ID: 1", exception.getMessage());
        verify(notificacionRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarNotificacion_Success() {
        // Arrange
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        // Act
        NotificacionDTO updatedDTO = new NotificacionDTO();
        updatedDTO.setUsuarioId(1L);
        updatedDTO.setMensaje("Tu pedido ha sido entregado!");

        NotificacionDTO result = notificacionService.actualizarNotificacion(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getUsuarioId());
        assertEquals("Tu pedido ha sido entregado!", result.getMensaje());
        verify(notificacionRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void testActualizarNotificacion_NotificacionNotFound() {
        // Arrange
        when(notificacionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificacionService.actualizarNotificacion(1L, notificacionDTO);
        });
        assertEquals("Notificación no encontrada con ID: 1", exception.getMessage());
        verify(notificacionRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).findById(anyLong());
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    void testActualizarNotificacion_UsuarioNotFound() {
        // Arrange
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificacionService.actualizarNotificacion(1L, notificacionDTO);
        });
        assertEquals("Usuario no encontrado con ID: 1", exception.getMessage());
        verify(notificacionRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    void testEliminarNotificacion_Success() {
        // Arrange
        when(notificacionRepository.existsById(1L)).thenReturn(true);

        // Act
        notificacionService.eliminarNotificacion(1L);

        // Assert
        verify(notificacionRepository, times(1)).existsById(1L);
        verify(notificacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarNotificacion_NotFound() {
        // Arrange
        when(notificacionRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificacionService.eliminarNotificacion(1L);
        });
        assertEquals("Notificación no encontrada con ID: 1", exception.getMessage());
        verify(notificacionRepository, times(1)).existsById(1L);
        verify(notificacionRepository, never()).deleteById(anyLong());
    }

    @Test
    void testObtenerNotificacionesPorUsuario() {
        // Arrange
        when(notificacionRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(notificacion));

        // Act
        List<NotificacionDTO> result = notificacionService.obtenerNotificacionesPorUsuario(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        assertEquals("Tu pedido está listo!", result.get(0).getMensaje());
        verify(notificacionRepository, times(1)).findByUsuarioId(1L);
    }

    @Test
    void testObtenerNotificacionesPorUsuario_NoNotificaciones() {
        // Arrange
        when(notificacionRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList());

        // Act
        List<NotificacionDTO> result = notificacionService.obtenerNotificacionesPorUsuario(1L);

        // Assert
        assertEquals(0, result.size());
        verify(notificacionRepository, times(1)).findByUsuarioId(1L);
    }
}