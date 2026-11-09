package com.pwparcial.palcial.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.pwparcial.palcial.model.Usuario;
import com.pwparcial.palcial.repository.UsuarioRepo;

@Configuration
public class Seguridad {

	@Autowired
	private UsuarioRepo repo;

	@Bean
	public SecurityFilterChain filtro(HttpSecurity http) throws Exception {
		http.csrf(c -> c.disable())
			.authorizeHttpRequests(a -> a
				.requestMatchers("/login.html", "/sinpermiso.html", "/estilos.css").permitAll()
				.requestMatchers("/admin.html").hasRole("ADMINISTRADOR")
				.requestMatchers("/medico.html").hasRole("MEDICO")
				.requestMatchers("/paciente.html").hasRole("PACIENTE")
				.requestMatchers(HttpMethod.GET, "/api/**").authenticated()
				.requestMatchers(HttpMethod.PUT, "/api/consultas/*/horario").hasRole("PACIENTE")
				.requestMatchers("/api/**").hasRole("ADMINISTRADOR")
				.anyRequest().authenticated())
			.formLogin(f -> f.loginPage("/login.html").loginProcessingUrl("/login")
				.defaultSuccessUrl("/", true).failureUrl("/login.html?error").permitAll())
			// al cerrar sesion se invalida la sesion y se borra la cookie
			.logout(l -> l.logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true)
				.deleteCookies("SESSION", "JSESSIONID").logoutSuccessUrl("/login.html"))
			// la api responde 401 y 403 directo; las paginas van al login o a sinpermiso.html
			.exceptionHandling(e -> e
				.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), api)
				.defaultAccessDeniedHandlerFor((req, res, ex) -> res.setStatus(403), api)
				.defaultAccessDeniedHandlerFor(sinPermiso(), AnyRequestMatcher.INSTANCE));
		return http.build();
	}

	private RequestMatcher api = PathPatternRequestMatcher.withDefaults().matcher("/api/**");

	private AccessDeniedHandlerImpl sinPermiso() {
		AccessDeniedHandlerImpl h = new AccessDeniedHandlerImpl();
		h.setErrorPage("/sinpermiso.html");
		return h;
	}

	@Bean
	public UserDetailsService usuarios() {
		return username -> {
			Usuario u = repo.findByUsername(username);
			if (u == null) throw new UsernameNotFoundException(username);
			return User.withUsername(u.getUsername()).password(u.getPassword()).roles(u.getRol()).build();
		};
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
