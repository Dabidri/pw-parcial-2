package com.pwparcial.palcial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.pwparcial.palcial.config.Datos;
import com.pwparcial.palcial.model.Consulta;
import com.pwparcial.palcial.repository.ConsultaRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Consultas", description = "Registro de consultas medicas (consultorio, horario, medico y paciente)")
@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

	@Autowired
	private ConsultaRepo repo;

	@Autowired
	private Datos datos;

	@Operation(summary = "Listar consultas", description = "ADMINISTRADOR ve todas, MEDICO solo las asignadas a el y PACIENTE solo las suyas.")
	@GetMapping
	public List<Consulta> lista(Authentication a) {
		String rol = a.getAuthorities().iterator().next().getAuthority();
		if (rol.equals("ROLE_MEDICO")) return repo.findByMedicoUsuarioUsername(a.getName());
		if (rol.equals("ROLE_PACIENTE")) return repo.findByCuentaUsername(a.getName());
		return repo.findAll();
	}

	@Operation(summary = "Buscar consulta", description = "Devuelve una consulta por id. Responde 403 si no es una consulta que el usuario pueda ver.")
	@GetMapping("/{id}")
	public ResponseEntity<Consulta> uno(@PathVariable Long id, Authentication a) {
		return lista(a).stream().filter(c -> c.getId().equals(id)).findFirst()
			.map(ResponseEntity::ok).orElse(ResponseEntity.status(403).build());
	}

	@Operation(summary = "Crear consulta", description = "Solo ADMINISTRADOR. La cita queda para la cuenta PACIENTE cuyo usuario es el nombre del paciente "
		+ "(si no existe se crea con clave nombre + 123). Ej: {\"paciente\":\"Juan Perez\",\"motivo\":\"Dolor de cabeza\",\"consultorio\":101,"
		+ "\"horaInicio\":\"08:00\",\"horaFin\":\"08:30\",\"medico\":{\"id\":1}}")
	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody Consulta c) {
		return guardar(c);
	}

	@Operation(summary = "Editar consulta", description = "Solo ADMINISTRADOR. Mismo cuerpo que crear consulta.")
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody Consulta c) {
		c.setId(id);
		return guardar(c);
	}

	// liga (o crea) la cuenta PACIENTE con el nombre del paciente
	private ResponseEntity<?> guardar(Consulta c) {
		c.setCuenta(datos.cuenta(c.getPaciente(), "PACIENTE"));
		if (c.getCuenta() == null) return ResponseEntity.badRequest().body("- El nombre " + c.getPaciente() + " ya es un usuario con otro rol\n");
		return ResponseEntity.ok(repo.save(c));
	}

	@Operation(summary = "Cambiar horario", description = "Solo PACIENTE y solo en sus propias consultas (si no, 403). Responde 400 si la hora no es HH:mm. "
		+ "Ej: {\"horaInicio\":\"09:00\",\"horaFin\":\"09:30\"}")
	@PutMapping("/{id}/horario")
	public ResponseEntity<?> horario(@PathVariable Long id, @RequestBody Consulta h, Authentication a) {
		Consulta c = repo.findById(id).orElse(null);
		if (c == null || c.getCuenta() == null || !c.getCuenta().getUsername().equals(a.getName())) return ResponseEntity.status(403).build();
		String res = "";
		if (h.getHoraInicio() == null || !h.getHoraInicio().matches(Consulta.HORA)) res += "- La hora de inicio debe tener formato HH:mm. Ej: 08:00\n";
		if (h.getHoraFin() == null || !h.getHoraFin().matches(Consulta.HORA)) res += "- La hora de finalizacion debe tener formato HH:mm. Ej: 08:30\n";
		if (res.isEmpty() && !h.isHorarioValido()) res += "- La hora de finalizacion debe ser mayor que la de inicio. Ej: 08:00 a 08:30\n";
		if (!res.isEmpty()) return ResponseEntity.badRequest().body(res);
		c.setHoraInicio(h.getHoraInicio());
		c.setHoraFin(h.getHoraFin());
		return ResponseEntity.ok(repo.save(c));
	}

	@Operation(summary = "Eliminar consulta", description = "Solo ADMINISTRADOR. Elimina la consulta por id.")
	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable Long id) {
		repo.deleteById(id);
	}
}
