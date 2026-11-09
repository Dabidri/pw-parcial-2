package com.pwparcial.palcial.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pwparcial.palcial.model.Medico;
import com.pwparcial.palcial.model.Usuario;
import com.pwparcial.palcial.repository.MedicoRepo;
import com.pwparcial.palcial.repository.UsuarioRepo;

@Component
public class Datos implements CommandLineRunner {

	@Autowired
	private UsuarioRepo repo;

	@Autowired
	private MedicoRepo medicos;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void run(String... args) {
		if (repo.count() == 0) {
			usuario("admin", "ADMINISTRADOR");
			usuario("medico", "MEDICO");
			usuario("paciente", "PACIENTE");
		}
		if (medicos.count() == 0) {
			medico("Ana Torres", "Medicina General", repo.findByUsername("medico"));
			medico("Carlos Ruiz", "Pediatria", null);
		}
	}

	private Usuario usuario(String nombre, String rol) {
		Usuario u = new Usuario();
		u.setUsername(nombre);
		u.setPassword(encoder.encode(nombre + "123"));
		u.setRol(rol);
		return repo.save(u);
	}

	// busca la cuenta o la crea (clave = usuario + "123"); null si ya existe con otro rol
	public Usuario cuenta(String nombre, String rol) {
		Usuario u = repo.findByUsername(nombre);
		if (u == null) u = usuario(nombre, rol);
		return u.getRol().equals(rol) ? u : null;
	}

	private void medico(String nombre, String especialidad, Usuario u) {
		Medico m = new Medico();
		m.setNombre(nombre);
		m.setEspecialidad(especialidad);
		m.setUsuario(u);
		medicos.save(m);
	}
}
