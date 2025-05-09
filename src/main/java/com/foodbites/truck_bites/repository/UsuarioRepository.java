package com.foodbites.truck_bites.repository;

import com.foodbites.truck_bites.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario, gestiona operaciones CRUD y consultas personalizadas.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Encuentra un usuario por su email.
     * @param email El email del usuario.
     * @return Un Optional con el usuario encontrado, o vacío si no existe.
     */
    Optional<Usuario> findByEmail(String email);
}