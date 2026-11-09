package com.pwparcial.palcial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Medico {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "El nombre del medico es obligatorio. Ej: Ana Torres")
	@Pattern(regexp = "^[\\p{L}][\\p{L} .]{2,59}$", message = "El nombre del medico solo admite letras, espacios y puntos (3 a 60). Ej: Ana Torres")
	private String nombre;

	@NotNull(message = "La especialidad es obligatoria. Ej: Pediatria")
	@Pattern(regexp = "^[\\p{L}][\\p{L} ]{2,39}$", message = "La especialidad solo admite letras y espacios (3 a 40). Ej: Pediatria")
	private String especialidad;

	// cuenta con la que el medico inicia sesion (opcional); se crea sola con rol MEDICO
	@ManyToOne
	private Usuario usuario;

	@JsonIgnore
	@AssertTrue(message = "El usuario del medico es obligatorio y solo admite letras y numeros (3 a 20). Ej: atorres")
	public boolean isUsuarioValido() {
		return usuario != null && usuario.getUsername() != null && usuario.getUsername().matches("^[A-Za-z0-9]{3,20}$");
	}
}
