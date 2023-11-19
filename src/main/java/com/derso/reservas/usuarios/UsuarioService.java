package com.derso.reservas.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.derso.reservas.autenticacao.JwtService;
import com.derso.reservas.autenticacao.SenhaInvalidaException;

@Service
public class UsuarioService implements UserDetailsService {
	
	private static final PasswordEncoder encoder = 
			new BCryptPasswordEncoder(12);
	
	@Autowired
	private UsuarioRepository repositorio;
	
	@Autowired
	private JwtService jwtService;
	
	public void salvar(UsuarioDTO usuarioDTO) {
		String senhaPelada = usuarioDTO.getSenha();
		String senhaCriptografada = encoder.encode(senhaPelada);
		Usuario usuario = new Usuario(
				usuarioDTO.getNomeCompleto(), usuarioDTO.getEmail(), senhaCriptografada);
		repositorio.save(usuario);
	}
	
	public String autenticar(UsuarioDTO usuarioDTO) 
			throws UsernameNotFoundException, SenhaInvalidaException {
		
		Usuario usuario = buscarUsuario(usuarioDTO.getEmail());
		String senhaDigitada = usuarioDTO.getSenha();
		String criptografada = usuario.getSenhaCriptografada().replace("{bcrypt}", "");
		
		if (encoder.matches(senhaDigitada, criptografada)) {
			return jwtService.gerarToken(usuario);
		}
		
		throw new SenhaInvalidaException();
	}
			
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = buscarUsuario(email);

		return User.builder()
			.username(email)
			.password("{bcrypt}" + usuario.getSenhaCriptografada())
			.build();
	}

	private Usuario buscarUsuario(String email) {
		Usuario usuario = repositorio
			.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
		return usuario;
	}

}
