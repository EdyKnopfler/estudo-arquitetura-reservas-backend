package com.derso.reservas.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.derso.reservas.autenticacao.SenhaInvalidaException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping("/novo")
	@Transactional
	public void novo(@RequestBody @Valid UsuarioDTO usuarioDTO) {
		usuarioService.salvar(usuarioDTO);
	}
	
	@PostMapping("/autenticar")
	public String autenticar(@RequestBody UsuarioDTO usuarioDTO) {
		try {
			return usuarioService.autenticar(usuarioDTO);
		} catch (UsernameNotFoundException | SenhaInvalidaException e) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
		}
	}

}
