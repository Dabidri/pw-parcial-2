package com.pwparcial.palcial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Consulta {
	public static final String HORA = "^([01][0-9]|2[0-3]):[0-5][0-9]$";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "El nombre del paciente es obligatorio. Ej: Juan Perez")
	@Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9 ]{0,39}$", message = "El nombre del paciente solo admite letras, numeros y espacios (max 40). Ej: Juan Perez")
	@Column(length = 40)
	private String paciente;

	@NotNull(message = "El motivo es obligatorio. Ej: Dolor de cabeza")
	@Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9 .,;:()?!-]{2,99}$", message = "El motivo debe tener de 3 a 100 caracteres de texto. Ej: Dolor de cabeza, fiebre")
	@Column(length = 100)
	private String motivo;

	@NotNull(message = "El numero de consultorio es obligatorio. Ej: 101")
	@Min(value = 1, message = "El consultorio debe ser mayor o igual a 1. Ej: 101")
	@Max(value = 999, message = "El consultorio debe ser menor o igual a 999. Ej: 101")
	private Integer consultorio;

	@NotNull(message = "La hora de inicio es obligatoria. Ej: 08:00")
	@Pattern(regexp = HORA, message = "La hora de inicio debe tener formato HH:mm. Ej: 08:00")
	private String horaInicio;

	@NotNull(message = "La hora de finalizacion es obligatoria. Ej: 08:30")
	@Pattern(regexp = HORA, message = "La hora de finalizacion debe tener formato HH:mm. Ej: 08:30")
	private String horaFin;

	@NotNull(message = "Seleccione un medico de la lista")
	@ManyToOne
	private Medico medico;

	// cuenta del paciente (su usuario es el nombre del paciente); se asigna sola al guardar
	@JsonIgnore
	@ManyToOne
	private Usuario cuenta;

	@JsonIgnore
	@AssertTrue(message = "La hora de finalizacion debe ser mayor que la de inicio. Ej: 08:00 a 08:30")
	public boolean isHorarioValido() {
		if (horaInicio == null || horaFin == null || !horaInicio.matches(HORA) || !horaFin.matches(HORA)) return true;
		return horaFin.compareTo(horaInicio) > 0;
	}
}
