package com.pwparcial.palcial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	@JsonIgnore
	private String password;

	// ADMINISTRADOR, MEDICO o PACIENTE
	private String rol;
}
