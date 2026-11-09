package com.pwparcial.palcial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pwparcial.palcial.model.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
	Usuario findByUsername(String username);

	List<Usuario> findByRol(String rol);
}
