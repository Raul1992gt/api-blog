package com.edix.apirestblog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.edix.apirestblog.dto.ComentarioDto;
import com.edix.apirestblog.entities.Comentario;
import com.edix.apirestblog.entities.Publicacion;
import com.edix.apirestblog.excepctions.BlogAppException;
import com.edix.apirestblog.excepctions.ResourceNotFoundException;
import com.edix.apirestblog.repositories.ComentarioRepository;
import com.edix.apirestblog.repositories.PublicacionRepository;

@Service
public class ComentarioServiceImplMy8 implements ComentarioService{

	@Autowired
	private ModelMapper mMap;
	
	@Autowired
	private ComentarioRepository cRep;
	
	@Autowired
	private PublicacionRepository pRep;
	
	@Override
	public ComentarioDto crearComentario(long publicacionId, ComentarioDto comentairoDTO) {
		Comentario comentario = mapearEntity(comentairoDTO);
		
		//Buscar por ID
		Publicacion publicacion = pRep.findById(publicacionId)
						.orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));	
				
		comentario.setPublicacion(publicacion);
		Comentario nuevoComentario = cRep.save(comentario);
		
		return mapearComentarioDto(nuevoComentario);
	}

	

	@Override
	public List<ComentarioDto> obtenerComentariosPorPublicacionId(long publicacionId) {
		List<Comentario> comentarios = cRep.findByPublicacionId(publicacionId);
				
		return comentarios.stream().map(comentario -> mapearComentarioDto(comentario)).collect(Collectors.toList());
	}

	@Override
	public ComentarioDto buscarComentarioPorCometarioId(long publicacionId, long comentarioId) {
		//Buscar por ID
		Publicacion publicacion = pRep.findById(publicacionId)
					.orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));	
						
		Comentario comentario = cRep.findById(comentarioId)
				.orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));
		
		if(!comentario.getPublicacion().getId().equals(publicacion.getId())) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicacion");
		}
		return mapearComentarioDto(comentario);
	}

	@Override
	public ComentarioDto actualizarComentario(long publicacionId, long comentarioId,ComentarioDto solicitudComentario) {
		// Buscar por ID
		Publicacion publicacion = pRep.findById(publicacionId)
				.orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));

		Comentario comentario = cRep.findById(comentarioId)
				.orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));

		if (!comentario.getPublicacion().getId().equals(publicacion.getId())) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicacion");
		}

		comentario.setNombre(solicitudComentario.getNombre());
		comentario.setEmail(solicitudComentario.getEmail());
		comentario.setCuerpo(solicitudComentario.getCuerpo());

		Comentario comentarioActualizado = cRep.save(comentario);

		return mapearComentarioDto(comentarioActualizado);
	}

	@Override
	public void eliminarComentario(long publicacionId, long comentarioId) {
		// Buscar por ID
				Publicacion publicacion = pRep.findById(publicacionId)
						.orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));

				Comentario comentario = cRep.findById(comentarioId)
						.orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));

				if (!comentario.getPublicacion().getId().equals(publicacion.getId())) {
					throw new BlogAppException(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicacion");
				}
		
				cRep.delete(comentario);
	}
	
	//Convertimos un comentario a comentarioDTO para transferir datos
		private ComentarioDto mapearComentarioDto(Comentario comentario) {
			
			ComentarioDto comentarioDTO = mMap.map(comentario, ComentarioDto.class);
			
			/*
			 * Para mapear una entidad a dto sin ModelMapper
			 
			ComentarioDto comentarioDTO = new ComentarioDto();
			
			comentarioDTO.setId(comentario.getId());
			comentarioDTO.setNombre(comentario.getNombre());
			comentarioDTO.setEmail(comentario.getEmail());
			comentarioDTO.setCuerpo(comentario.getCuerpo());
			*/
			return comentarioDTO;
		}
		
		//Convertimos un objeto de transferencia DTO a entidad
		private Comentario mapearEntity(ComentarioDto comentarioDTO) {
			
			Comentario comentario = mMap.map(comentarioDTO, Comentario.class);
			
			/*
			 * Para mapear un DTO a entidad sin ModelMapper
			Comentario comentario = new Comentario();
			
			comentario.setId(comentarioDTO.getId());
			comentario.setNombre(comentarioDTO.getNombre());
			comentario.setEmail(comentarioDTO.getEmail());
			comentario.setCuerpo(comentarioDTO.getCuerpo());
			comentario.setPublicacion(comentario.getPublicacion());
			*/
			return comentario;
		}
}
