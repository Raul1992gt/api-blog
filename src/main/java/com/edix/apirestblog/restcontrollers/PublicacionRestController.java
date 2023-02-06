package com.edix.apirestblog.restcontrollers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edix.apirestblog.dto.PublicacionDto;
import com.edix.apirestblog.dto.PublicacionResponse;
import com.edix.apirestblog.services.PublicacionService;
import com.edix.apirestblog.tools.AppConstants;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionRestController {

	@Autowired
	private PublicacionService pServ;

	@GetMapping
	public PublicacionResponse listarPublicaciones(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroPagina,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaPagina,
			@RequestParam(value = "sortBy",defaultValue = AppConstants.ORDENAR_POR_DEFECTO, required = false)String ordenarPor,
			@RequestParam(value = "sortDir",defaultValue = AppConstants.ORDENAR_DIRECCION_POR_DEFECTO, required = false)String sortDir){
		return pServ.obtenerLasPublicaciones(numeroPagina,medidaPagina,ordenarPor,sortDir);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PublicacionDto> obtenerPublicacionPorId(@PathVariable("id") long id) {
		return ResponseEntity.ok(pServ.obtenerPublicacionPorId(id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<PublicacionDto> guardarPublicacion(@Valid @RequestBody PublicacionDto publicDto) {

		return new ResponseEntity<>(pServ.crearPublicacion(publicDto), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<PublicacionDto> actualizarPublicacion(@Valid @RequestBody PublicacionDto publicacionDTO,
			@PathVariable("id") long id) {
		PublicacionDto publiacionResponse = pServ.actualizarPublicacion(publicacionDTO, id);

		return new ResponseEntity<>(publiacionResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminarPublicacion(@PathVariable("id") long id) {
		pServ.eliminarPublicacion(id);

		return new ResponseEntity<>("Publicacion eliminada con éxito", HttpStatus.OK);
	}
}
