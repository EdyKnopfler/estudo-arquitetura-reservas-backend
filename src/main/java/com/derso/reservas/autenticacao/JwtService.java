package com.derso.reservas.autenticacao;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.derso.reservas.usuarios.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${security.jwt.expiracao-em-minutos}")
	private String expiracaoEmMinutos;
	
	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;

	private SecretKey criarObjetoChave() {
		byte[] chaveEmBytes = chaveAssinatura.getBytes();
		return Keys.hmacShaKeyFor(chaveEmBytes);
	}
	
	public String gerarToken(Usuario usuario) {
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(
				Long.valueOf(expiracaoEmMinutos));
		
		Date data = Date.from(
				dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant());
		
		Map<String, Object> claims = new HashMap<>();
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(usuario.getEmail())
				.setExpiration(data)
				.signWith(criarObjetoChave(), SignatureAlgorithm.HS512)
				.compact();
	}
	
	public Claims decodificar(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(criarObjetoChave())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public boolean tokenValido(String token) {
		try {
			LocalDateTime dataHoraExpiracao = decodificar(token)
					.getExpiration()
					.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			
			return dataHoraExpiracao.isAfter(LocalDateTime.now());
		} catch (Exception e) {
			return false;
		}
	}
	
	public String loginUsuario(String token) {
		return decodificar(token).getSubject();
	}
	
}
