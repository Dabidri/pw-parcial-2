package com.pwparcial.palcial.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Errores {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> validacion(MethodArgumentNotValidException e) {
		String res = "";
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			res += "- " + f.getDefaultMessage() + "\n";
		}
		return ResponseEntity.badRequest().body(res);
	}


	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> integridad(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().body("- No se puede guardar o eliminar porque el registro esta relacionado con otros datos\n");
	}
}
