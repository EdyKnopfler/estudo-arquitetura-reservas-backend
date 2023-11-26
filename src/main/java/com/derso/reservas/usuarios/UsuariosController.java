package com.derso.reservas.usuarios;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<Map<String, String>> autenticar(@RequestBody UsuarioDTO usuarioDTO) {
		Map<String, String> resposta = new HashMap<>();
		
		try {
			String token = usuarioService.autenticar(usuarioDTO);
			resposta.put("status", "ok");
			resposta.put("token", token);
			return new ResponseEntity<>(resposta, HttpStatus.OK);
		} catch (UsernameNotFoundException | SenhaInvalidaException e) {
			resposta.put("status", "erro");
			resposta.put("erro", "Usuário ou senha inválidos");
			return new ResponseEntity<>(resposta, HttpStatus.UNAUTHORIZED);
		}
		
	}
	
}
