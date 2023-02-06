package com.edix.apirestblog.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.edix.apirestblog.excepctions.BlogAppException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	//Obtenemos el valor de la propiedad con @Value
	@Value("${app.jwt-secret")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-milliseconds}")
	private int jwtExpirationInMs;
	
	public String generarToken(Authentication auth) {
		String userName = auth.getName();
		Date fechaActual = new Date();
		Date fechaExpiracion = new Date(fechaActual.getTime() + jwtExpirationInMs);
		String token = Jwts.builder().setSubject(userName).setIssuedAt(new Date()).setExpiration(fechaExpiracion)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		
		return token;
	}
	
	public String obtenerUserNameDeJwt(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		
			//obtenemos el userName del toke
		return claims.getSubject();
	}
	
	public boolean validarToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "Firma JWT no válida");
		}catch (MalformedJwtException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "Token JWT no válido");
		}catch (ExpiredJwtException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "Token JWT caducado");
		}catch (UnsupportedJwtException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "Token JWT no compatible");
		}catch (IllegalArgumentException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "La cadena claims JWT está vacía");
		}
	}
	
	
}
