package com.pwparcial.palcial.config;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

// filtro servlet: el navegador no guarda las paginas en cache,
// asi al cerrar sesion y dar "atras" las vuelve a pedir al servidor
@Component
public class SinCache implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse r = (HttpServletResponse) res;
		r.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
		r.setHeader("Pragma", "no-cache");
		r.setDateHeader("Expires", 0);
		chain.doFilter(req, res);
	}
}
