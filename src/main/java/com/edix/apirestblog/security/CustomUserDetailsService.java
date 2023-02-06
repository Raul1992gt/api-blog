package com.edix.apirestblog.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.edix.apirestblog.entities.Rol;
import com.edix.apirestblog.entities.Usuario;
import com.edix.apirestblog.repositories.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository uRep;
	
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		Usuario usuario = uRep.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ese username o email : " + usernameOrEmail));
		return new User(usuario.getEmail(), usuario.getPassword(), mapearRoles(usuario.getRoles()));
	}

	
	private Collection<? extends GrantedAuthority> mapearRoles(Set<Rol> roles){
		return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getNombre())).collect(Collectors.toList());
	}
}
