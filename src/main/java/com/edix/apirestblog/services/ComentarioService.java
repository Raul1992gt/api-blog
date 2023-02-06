package com.edix.apirestblog.services;

import java.util.List;

import com.edix.apirestblog.dto.ComentarioDto;

public interface ComentarioService {

	public ComentarioDto crearComentario(long publicacionId, ComentarioDto comentairoDTO);
	
	public List<ComentarioDto> obtenerComentariosPorPublicacionId(long publicacionId);
	
	public ComentarioDto buscarComentarioPorCometarioId(long publicacionId,long comentarioId);
	
	public ComentarioDto actualizarComentario(long publicacionId, long comentarioId,ComentarioDto solicitudComentario);
	
	public void eliminarComentario(long publicacionId, long comentarioId);
		
	
	
}
