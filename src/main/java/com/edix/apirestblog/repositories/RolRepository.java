package com.edix.apirestblog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edix.apirestblog.entities.Rol;

public interface RolRepository extends JpaRepository<Rol, Long>{

	public Optional<Rol> findByNombre(String nombre);
	
	
}
