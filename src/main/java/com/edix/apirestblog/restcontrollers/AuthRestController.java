package com.edix.apirestblog.restcontrollers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edix.apirestblog.dto.JwtAuthResponseDto;
import com.edix.apirestblog.dto.LoginDto;
import com.edix.apirestblog.dto.RegistroDto;
import com.edix.apirestblog.entities.Rol;
import com.edix.apirestblog.entities.Usuario;
import com.edix.apirestblog.repositories.RolRepository;
import com.edix.apirestblog.repositories.UsuarioRepository;
import com.edix.apirestblog.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UsuarioRepository usuarioRepositorio;
	
	@Autowired
	private RolRepository rolRepositorio;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/iniciarSesion")
	public ResponseEntity<JwtAuthResponseDto> authenticateUser(@RequestBody LoginDto loginDTO){
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserNameOrEmail(), loginDTO.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		//Obtenemos el token de jwtTokenProvider
		String token = jwtTokenProvider.generarToken(auth);
		
		return ResponseEntity.ok(new JwtAuthResponseDto(token));
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<?> registrarUsuario(@RequestBody RegistroDto registroDTO){
		if(usuarioRepositorio.existsByUsername(registroDTO.getUserName())) {
			return new ResponseEntity<>("Ese nombre de usuario ya existe" , HttpStatus.BAD_REQUEST);
		}
		if(usuarioRepositorio.existsByEmail(registroDTO.getEmail())) {
			return new ResponseEntity<>("Ese email de usuario ya existe" , HttpStatus.BAD_REQUEST);
		} 
		
		Usuario usuario = new Usuario();
		usuario.setNombre(registroDTO.getNombre());
		usuario.setUsername(registroDTO.getUserName());
		usuario.setEmail(registroDTO.getEmail());
		usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
		
		Rol roles = rolRepositorio.findByNombre("ROLE_ADMIN").get();
		usuario.setRoles(Collections.singleton(roles));
		
		usuarioRepositorio.save(usuario);
		
		return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.OK);
	}
}
