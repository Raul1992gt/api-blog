package com.edix.apirestblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edix.apirestblog.entities.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long>{

	public List<Comentario> findByPublicacionId(long publicacionId);
}
