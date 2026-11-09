package com.pwparcial.palcial.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;

@Configuration
public class Swagger {

	@Bean
	public OpenAPI api() {
		return new OpenAPI().info(new Info()
			.title("Consultas Medicas - Documentacion de servicios")
			.version("1.0")
			.description("Gestion de consultas medicas con roles ADMINISTRADOR, MEDICO y PACIENTE. "
				+ "Primero hay que iniciar sesion en /login.html (admin/admin123, medico/medico123, paciente/paciente123) "
				+ "y luego volver a esta pagina para probar los servicios."));
	}


	@Bean
	public OpenApiCustomizer codigos() {
		return api -> api.getPaths().values().forEach(p -> p.readOperations().forEach(o -> {
			o.getResponses().putIfAbsent("200", new ApiResponse().description("OK"));
			o.getResponses().put("401", new ApiResponse().description("Unauthorized: no ha iniciado sesion"));
			o.getResponses().put("403", new ApiResponse().description("Forbidden: su rol no tiene permiso para este servicio"));
		}));
	}
}
