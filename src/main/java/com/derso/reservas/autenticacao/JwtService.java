package com.derso.reservas.autenticacao;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.derso.reservas.usuarios.Usuario;

@Service
public class JwtService {

	private static final String ISSUER = "reservas-derso";

	@Value("${security.jwt.expiracao-em-minutos}")
	private String expiracaoEmMinutos;

	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;

	public String gerarToken(Usuario usuario) {
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(
				Long.valueOf(expiracaoEmMinutos));

		Instant instantExpiracao = dataHoraExpiracao.atZone(
				ZoneId.systemDefault()).toInstant();

		String token = JWT
				.create()
				.withIssuer(ISSUER)
				.withSubject(usuario.getEmail())
				.withPayload(usuario.toJwtPayload())
				.withExpiresAt(instantExpiracao)
				.sign(algoritmoAssinatura());

		return token;
	}

	public DecodedJWT decodificar(String token) {
		return JWT
				.require(algoritmoAssinatura())
				.withIssuer(ISSUER)
				.build()
				.verify(token);
	}

	private Algorithm algoritmoAssinatura() {
		return Algorithm.HMAC256(chaveAssinatura);
	}

	public boolean tokenValido(DecodedJWT parseado) {
		Instant instanteExpiracao = parseado.getExpiresAtAsInstant();
		return instanteExpiracao.isAfter(Instant.now());
	}

	public String loginUsuario(DecodedJWT parseado) {
		return parseado.getSubject();
	}

}
