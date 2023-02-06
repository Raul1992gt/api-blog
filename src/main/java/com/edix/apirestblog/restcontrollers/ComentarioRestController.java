package com.edix.apirestblog.restcontrollers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edix.apirestblog.dto.ComentarioDto;
import com.edix.apirestblog.services.ComentarioService;

@RestController
@RequestMapping("/api")
public class ComentarioRestController {

	@Autowired
	private ComentarioService cServ;
	
	@GetMapping("/publicaciones/{publicacionId}/comentarios")
	public List<ComentarioDto> listarComentariosPorPublicacionId(@PathVariable(value = "publicacionId") long publicacionId){
		return cServ.obtenerComentariosPorPublicacionId(publicacionId);
	}
	
	@GetMapping("/publicaciones/{publicacionId}/comentarios/{id}")
	public ResponseEntity<ComentarioDto> obtenerComentarioPorId(@PathVariable(value = "publicacionId")long publicacionId, @PathVariable(value = "id") long comentarioId){
		ComentarioDto comentarioDTO = cServ.buscarComentarioPorCometarioId(publicacionId, comentarioId);
		
		return new ResponseEntity<>(comentarioDTO,HttpStatus.OK);
		//return new ResponseEntity<>(cServ.buscarComentarioPorCometarioId(publicacionId, comentarioId),HttpStatus.OK);
	}
	
	@PostMapping("/publicaciones/{publicacionId}/comentarios")
	public ResponseEntity<ComentarioDto> guardarComentario(@PathVariable(value = "publicacionId")long publicacionId,@Valid @RequestBody ComentarioDto comentarioDTO){
		
		return new ResponseEntity<>(cServ.crearComentario(publicacionId, comentarioDTO),HttpStatus.CREATED);
		
	}
	
	@PutMapping("/publicaciones/{publicacionId}/comentarios/{id}")
	public ResponseEntity<ComentarioDto> actualizarComentario(@PathVariable(value = "publicacionId")long publicacionId, @PathVariable(value = "id") long comentarioId,@Valid @RequestBody ComentarioDto comentairoDTO){
		ComentarioDto comentarioActualizado = cServ.actualizarComentario(publicacionId, comentarioId, comentairoDTO);
		return new ResponseEntity<>(comentarioActualizado,HttpStatus.OK);
	}
	
	@DeleteMapping("/publicaciones/{publicacionId}/comentarios/{id}")
	public ResponseEntity<String> eliminarComentario(@PathVariable(value = "publicacionId")long publicacionId, @PathVariable(value = "id") long comentarioId){
		cServ.eliminarComentario(publicacionId, comentarioId);
		
		return new ResponseEntity<>("Comentario eliminado, con éxito",HttpStatus.OK);
	}
}
