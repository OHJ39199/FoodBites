package com.foodbites.truck_bites.service;

import com.foodbites.truck_bites.dto.NotificacionDTO;
import com.foodbites.truck_bites.model.Notificacion;
import com.foodbites.truck_bites.model.Usuario;
import com.foodbites.truck_bites.repository.NotificacionRepository;
import com.foodbites.truck_bites.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con notificaciones.
 */
@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacionService(NotificacionRepository notificacionRepository, UsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public NotificacionDTO crearNotificacion(NotificacionDTO notificacionDTO) {
        Optional<Usuario> usuario = usuarioRepository.findById(notificacionDTO.getUsuarioId());
        if (usuario.isPresent()) {
            Notificacion notificacion = new Notificacion();
            notificacion.setUsuario(usuario.get());
            notificacion.setMensaje(notificacionDTO.getMensaje());
            notificacion = notificacionRepository.save(notificacion);
            return toDTO(notificacion);
        }
        throw new IllegalArgumentException("Usuario no encontrado con ID: " + notificacionDTO.getUsuarioId());
    }

    public List<NotificacionDTO> obtenerNotificaciones() {
        return notificacionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public NotificacionDTO obtenerNotificacionPorId(Long id) {
        Optional<Notificacion> notificacion = notificacionRepository.findById(id);
        if (notificacion.isPresent()) {
            return toDTO(notificacion.get());
        }
        throw new IllegalArgumentException("Notificación no encontrada con ID: " + id);
    }

    @Transactional
    public NotificacionDTO actualizarNotificacion(Long id, NotificacionDTO notificacionDTO) {
        Optional<Notificacion> notificacionOpt = notificacionRepository.findById(id);
        if (notificacionOpt.isPresent()) {
            Notificacion notificacion = notificacionOpt.get();
            Optional<Usuario> usuario = usuarioRepository.findById(notificacionDTO.getUsuarioId());
            if (usuario.isPresent()) {
                notificacion.setUsuario(usuario.get());
                notificacion.setMensaje(notificacionDTO.getMensaje());
                notificacion = notificacionRepository.save(notificacion);
                return toDTO(notificacion);
            }
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + notificacionDTO.getUsuarioId());
        }
        throw new IllegalArgumentException("Notificación no encontrada con ID: " + id);
    }

    @Transactional
    public void eliminarNotificacion(Long id) {
        if (notificacionRepository.existsById(id)) {
            notificacionRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Notificación no encontrada con ID: " + id);
        }
    }

    public List<NotificacionDTO> obtenerNotificacionesPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private NotificacionDTO toDTO(Notificacion notificacion) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(notificacion.getId());
        dto.setUsuarioId(notificacion.getUsuario().getId());
        dto.setMensaje(notificacion.getMensaje());
        dto.setFechaEnvio(notificacion.getFechaEnvio());
        return dto;
    }
}
