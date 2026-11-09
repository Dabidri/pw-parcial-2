package com.pwparcial.palcial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pwparcial.palcial.model.Consulta;

public interface ConsultaRepo extends JpaRepository<Consulta, Long> {
	List<Consulta> findByMedicoUsuarioUsername(String username);

	List<Consulta> findByCuentaUsername(String username);
}
