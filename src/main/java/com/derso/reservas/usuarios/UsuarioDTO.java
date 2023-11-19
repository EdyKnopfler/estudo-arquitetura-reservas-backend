package com.derso.reservas.usuarios;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
	
	@NotEmpty(message = "O nome completo é obrigatório")
	String nomeCompleto;
	
	@NotEmpty(message = "O e-mail é obrigatório")
	String email;
	
	@NotEmpty(message = "A senha é obrigatória")
	String senha;

}
