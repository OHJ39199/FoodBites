import com.foodbites.truck_bites.dto.UsuarioDTO;
import com.foodbites.truck_bites.model.Usuario;
import com.foodbites.truck_bites.repository.UsuarioRepository;
import com.foodbites.truck_bites.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("John Doe");
        usuario.setEmail("john.doe@example.com");
        usuario.setPassword("password123");
        usuario.setUbicacion("Madrid");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("John Doe");
        usuarioDTO.setEmail("john.doe@example.com");
        usuarioDTO.setPassword("password123");
        usuarioDTO.setUbicacion("Madrid");
    }

    @Test
    void testCrearUsuario_Success() {
        // Arrange
        when(usuarioRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        UsuarioDTO result = usuarioService.crearUsuario(usuarioDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getNombre());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("password123", result.getPassword());
        assertEquals("Madrid", result.getUbicacion());
        verify(usuarioRepository, times(1)).findByEmail("john.doe@example.com");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCrearUsuario_EmailAlreadyExists() {
        // Arrange
        when(usuarioRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(usuario));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario(usuarioDTO);
        });
        assertEquals("El email ya está registrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail("john.doe@example.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testObtenerUsuarios() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        // Act
        List<UsuarioDTO> result = usuarioService.obtenerUsuarios();

        // Assert
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getNombre());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testObtenerUsuarioPorId_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        UsuarioDTO result = usuarioService.obtenerUsuarioPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getNombre());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerUsuarioPorId_NotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.obtenerUsuarioPorId(1L);
        });
        assertEquals("Usuario no encontrado con ID: 1", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarUsuario_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        UsuarioDTO updatedDTO = new UsuarioDTO();
        updatedDTO.setNombre("Jane Doe");
        updatedDTO.setEmail("jane.doe@example.com");
        updatedDTO.setPassword("newpassword123");
        updatedDTO.setUbicacion("Barcelona");

        UsuarioDTO result = usuarioService.actualizarUsuario(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Jane Doe", result.getNombre());
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("newpassword123", result.getPassword());
        assertEquals("Barcelona", result.getUbicacion());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testActualizarUsuario_NotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.actualizarUsuario(1L, usuarioDTO);
        });
        assertEquals("Usuario no encontrado con ID: 1", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testEliminarUsuario_Success() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // Act
        usuarioService.eliminarUsuario(1L);

        // Assert
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarUsuario_NotFound() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.eliminarUsuario(1L);
        });
        assertEquals("Usuario no encontrado con ID: 1", exception.getMessage());
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void testObtenerUsuarioPorEmail_Success() {
        // Arrange
        when(usuarioRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(usuario));

        // Act
        UsuarioDTO result = usuarioService.obtenerUsuarioPorEmail("john.doe@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getNombre());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(usuarioRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testObtenerUsuarioPorEmail_NotFound() {
        // Arrange
        when(usuarioRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.obtenerUsuarioPorEmail("john.doe@example.com");
        });
        assertEquals("Usuario no encontrado con email: john.doe@example.com", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail("john.doe@example.com");
    }
}