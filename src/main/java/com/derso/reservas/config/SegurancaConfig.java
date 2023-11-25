package com.derso.reservas.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.derso.reservas.autenticacao.JwtAuthFilter;
import com.derso.reservas.autenticacao.JwtService;
import com.derso.reservas.usuarios.UsuarioService;

@Configuration
public class SegurancaConfig {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private JwtService jwtService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AntPathRequestMatcher[] permitidosMatchers = Arrays.asList(
			antMatcher("/usuarios/novo"),
			antMatcher("/usuarios/autenticar")
		).toArray(AntPathRequestMatcher[]::new);
		
		return http
			.csrf(csrf -> csrf.disable())
				
			.authorizeHttpRequests(
				autorizacao -> autorizacao
					.requestMatchers(permitidosMatchers).permitAll()
					.anyRequest().authenticated()
			)
			
			.addFilterBefore(
					new JwtAuthFilter(jwtService, usuarioService),
					UsernamePasswordAuthenticationFilter.class)
			
			.sessionManagement((session) -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			
			.build();
	}
	
}
