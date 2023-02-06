package com.edix.apirestblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edix.apirestblog.entities.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long>{

}
