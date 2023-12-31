package com.derso.reservas.usuarios;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;
	
	private String nomeCompleto;
	
	@Column(unique = true)
	private String email;

	private String senhaCriptografada;
	
	public Usuario() {}
	
	public Usuario(String nomeCompleto, String email, String senhaCriptografada) {
		this.nomeCompleto = nomeCompleto;
		this.email = email;
		this.senhaCriptografada = senhaCriptografada;
	}

	public Map<String, String> toJwtPayload() {
		Map<String, String> payload = new HashMap<>();
		payload.put("id", uuid);
		payload.put("nome", nomeCompleto);
		payload.put("email", email);
		return payload;
	}
	
}
