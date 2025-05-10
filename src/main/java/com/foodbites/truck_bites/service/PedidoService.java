package com.foodbites.truck_bites.service;

import com.foodbites.truck_bites.dto.PedidoDTO;
import com.foodbites.truck_bites.model.Pedido;
import com.foodbites.truck_bites.model.Usuario;
import com.foodbites.truck_bites.model.FoodTruck;
import com.foodbites.truck_bites.repository.PedidoRepository;
import com.foodbites.truck_bites.repository.UsuarioRepository;
import com.foodbites.truck_bites.repository.FoodTruckRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con pedidos.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FoodTruckRepository foodTruckRepository;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository, FoodTruckRepository foodTruckRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.foodTruckRepository = foodTruckRepository;
    }

    @Transactional
    public PedidoDTO crearPedido(PedidoDTO pedidoDTO) {
        Optional<Usuario> usuario = usuarioRepository.findById(pedidoDTO.getUsuarioId());
        Optional<FoodTruck> foodTruck = foodTruckRepository.findById(pedidoDTO.getFoodTruckId());
        if (usuario.isPresent() && foodTruck.isPresent()) {
            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario.get());
            pedido.setFoodTruck(foodTruck.get());
            pedido.setItems(pedidoDTO.getItems());
            pedido.setMontoTotal(pedidoDTO.getMontoTotal());
            pedido.setEstado(pedidoDTO.getEstado() != null ? pedidoDTO.getEstado() : "PENDIENTE");
            pedido.setFechaProgramada(pedidoDTO.getFechaProgramada());
            pedido = pedidoRepository.save(pedido);
            return toDTO(pedido);
        }
        throw new IllegalArgumentException("Usuario o FoodTruck no encontrado");
    }

    public List<PedidoDTO> obtenerPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO obtenerPedidoPorId(Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isPresent()) {
            return toDTO(pedido.get());
        }
        throw new IllegalArgumentException("Pedido no encontrado con ID: " + id);
    }

    @Transactional
    public PedidoDTO actualizarPedido(Long id, PedidoDTO pedidoDTO) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            Optional<Usuario> usuario = usuarioRepository.findById(pedidoDTO.getUsuarioId());
            Optional<FoodTruck> foodTruck = foodTruckRepository.findById(pedidoDTO.getFoodTruckId());
            if (usuario.isPresent() && foodTruck.isPresent()) {
                pedido.setUsuario(usuario.get());
                pedido.setFoodTruck(foodTruck.get());
                pedido.setItems(pedidoDTO.getItems());
                pedido.setMontoTotal(pedidoDTO.getMontoTotal());
                pedido.setEstado(pedidoDTO.getEstado());
                pedido.setFechaProgramada(pedidoDTO.getFechaProgramada());
                pedido = pedidoRepository.save(pedido);
                return toDTO(pedido);
            }
            throw new IllegalArgumentException("Usuario o FoodTruck no encontrado");
        }
        throw new IllegalArgumentException("Pedido no encontrado con ID: " + id);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + id);
        }
    }

    public List<PedidoDTO> obtenerPedidosPendientes(Long foodTruckId) {
        return pedidoRepository.findPendingByFoodTruckId(foodTruckId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Double obtenerVentasTotales(Long foodTruckId, LocalDateTime startDate, LocalDateTime endDate) {
        Double total = pedidoRepository.calculateTotalSalesByFoodTruck(foodTruckId, startDate, endDate);
        return total != null ? total : 0.0;
    }

    public List<PedidoDTO> obtenerPedidosPorUsuarioYFoodTruck(Long usuarioId, Long foodTruckId) {
        return pedidoRepository.findByUsuarioIdAndFoodTruckId(usuarioId, foodTruckId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> obtenerPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PedidoDTO toDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setUsuarioId(pedido.getUsuario().getId());
        dto.setFoodTruckId(pedido.getFoodTruck().getId());
        dto.setFoodTruckNombre(pedido.getFoodTruck().getNombre());
        dto.setItems(pedido.getItems());
        dto.setMontoTotal(pedido.getMontoTotal());
        dto.setEstado(pedido.getEstado());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setFechaProgramada(pedido.getFechaProgramada());
        return dto;
    }
}