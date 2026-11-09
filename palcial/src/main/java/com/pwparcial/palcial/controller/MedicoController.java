package com.pwparcial.palcial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pwparcial.palcial.config.Datos;
import com.pwparcial.palcial.model.Medico;
import com.pwparcial.palcial.repository.MedicoRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Medicos", description = "Gestion de medicos. Tambien es la lista desplegable del formulario de consultas")
@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

	@Autowired
	private MedicoRepo repo;

	@Autowired
	private Datos datos;

	@Operation(summary = "Listar medicos", description = "Devuelve todos los medicos. Lo usa cualquier usuario autenticado (lista desplegable de consultas).")
	@GetMapping
	public List<Medico> lista() {
		return repo.findAll();
	}

	@Operation(summary = "Buscar medico", description = "Devuelve un medico por su id. Cualquier usuario autenticado.")
	@GetMapping("/{id}")
	public Medico uno(@PathVariable Long id) {
		return repo.findById(id).orElse(null);
	}

	@Operation(summary = "Crear medico", description = "Solo ADMINISTRADOR. El usuario es obligatorio: si no existe se crea con rol MEDICO y clave usuario + 123. "
		+ "Ej: {\"nombre\":\"Ana Torres\",\"especialidad\":\"Pediatria\",\"usuario\":{\"username\":\"atorres\"}}")
	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody Medico m) {
		return guardar(m);
	}

	@Operation(summary = "Editar medico", description = "Solo ADMINISTRADOR. Mismo cuerpo que crear medico. Ej: {\"nombre\":\"Ana Torres\",\"especialidad\":\"Cardiologia\",\"usuario\":{\"username\":\"atorres\"}}")
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody Medico m) {
		m.setId(id);
		return guardar(m);
	}

	// liga (o crea) la cuenta MEDICO a partir del usuario escrito (ya validado en Medico)
	private ResponseEntity<?> guardar(Medico m) {
		String u = m.getUsuario().getUsername();
		m.setUsuario(datos.cuenta(u, "MEDICO"));
		if (m.getUsuario() == null) return ResponseEntity.badRequest().body("- El usuario " + u + " ya existe con otro rol\n");
		return ResponseEntity.ok(repo.save(m));
	}

	@Operation(summary = "Eliminar medico", description = "Solo ADMINISTRADOR. Responde 400 si el medico tiene consultas asignadas.")
	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable Long id) {
		repo.deleteById(id);
	}
}
