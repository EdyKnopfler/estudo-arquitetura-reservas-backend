package com.derso.reservas.autenticacao;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.derso.reservas.usuarios.UsuarioService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * FILTRO DE AUTENTICAÇÃO
 */

public class JwtAuthFilter extends OncePerRequestFilter {
	
	private JwtService jwtService;
	private UsuarioService usuarioService;
	
	// Usada na configuração do Spring Boot (ver SegurancaConfig.java)
	public JwtAuthFilter(JwtService jwtService, UsuarioService usuarioService) {
		this.jwtService = jwtService;
		this.usuarioService = usuarioService;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorization = request.getHeader("Authorization");
		
		if (authorization != null && authorization.startsWith("Bearer")) {
			String token = authorization.split(" ")[1];
			DecodedJWT parseado = jwtService.parseToken(token);
			
			if (jwtService.tokenValido(parseado)) {
				String login = jwtService.loginUsuario(parseado);
				UserDetails usuario = usuarioService.loadUserByUsername(login);
				
				UsernamePasswordAuthenticationToken userAuthentication =
						new UsernamePasswordAuthenticationToken(
								usuario, null, usuario.getAuthorities());
				
				userAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(userAuthentication);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
