package com.pwparcial.palcial.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Inicio {

	@GetMapping("/")
	public String inicio(Authentication a) {
		String rol = a.getAuthorities().iterator().next().getAuthority();
		if (rol.equals("ROLE_ADMINISTRADOR")) return "redirect:/admin.html";
		if (rol.equals("ROLE_MEDICO")) return "redirect:/medico.html";
		return "redirect:/paciente.html";
	}
}
