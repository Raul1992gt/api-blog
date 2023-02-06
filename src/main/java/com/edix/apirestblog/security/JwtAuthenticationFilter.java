package com.edix.apirestblog.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private CustomUserDetailsService customDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//Obtenemose el token, de la solicitud http
		String token = obtenerJWTHttp(request);				
		
		//Validamos el token
		if(StringUtils.hasText(token) && jwtTokenProvider.validarToken(token)) {
			//Obtenemos el username del token
			String userName = jwtTokenProvider.obtenerUserNameDeJwt(token);
			
			//Cargamos el usuario asociado al token
			UserDetails userDetails = customDetailsService.loadUserByUsername(userName);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			//Establecemos la seguridad
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		
		filterChain.doFilter(request, response);
		
	}

	//Formato bearer token de acceso
	private String obtenerJWTHttp(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			//el 7, es que bearer son 6 caracteres mas 1 que deja postman  despues añade el token
			return bearerToken.substring(7,bearerToken.length());
		}
		
		return null;
	}
}
