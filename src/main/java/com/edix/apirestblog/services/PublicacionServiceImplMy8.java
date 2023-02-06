package com.edix.apirestblog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.edix.apirestblog.dto.PublicacionDto;
import com.edix.apirestblog.dto.PublicacionResponse;
import com.edix.apirestblog.entities.Publicacion;
import com.edix.apirestblog.excepctions.ResourceNotFoundException;
import com.edix.apirestblog.repositories.PublicacionRepository;

@Service
public class PublicacionServiceImplMy8 implements PublicacionService {

	@Autowired
	private ModelMapper mMap;
	
	@Autowired
	private PublicacionRepository pRep;

	/*
	 * Usamos DTO para la transferencia de datos, recibimos JSON, lo convertimos en
	 * entidad persistimos en la bbdd, y después lo convertimos en DTO de nuevo,
	 * para su transferencia
	 */
	@Override
	public PublicacionDto crearPublicacion(PublicacionDto publicacionDTO) {
		Publicacion publicacion = mapearEntity(publicacionDTO);

		Publicacion nuevaPublicacion = pRep.save(publicacion);

		PublicacionDto publicacionResponse = mapearDTO(nuevaPublicacion);

		return publicacionResponse;
	}

	@Override
	public PublicacionResponse obtenerLasPublicaciones(int numeroPagina, int medidaPagina, String ordenarPor, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(ordenarPor).ascending():Sort.by(ordenarPor).descending();
		
		Pageable pageable = PageRequest.of(numeroPagina, medidaPagina, sort);
		
		Page<Publicacion> publicaciones = pRep.findAll(pageable);
		
		List<Publicacion> listaPublicaciones = publicaciones.getContent();
		List<PublicacionDto> contenido =  listaPublicaciones.stream().map(publicacion -> mapearDTO(publicacion)).collect(Collectors.toList());
		
		PublicacionResponse publicacionResponse = new PublicacionResponse();
		publicacionResponse.setContenido(contenido);
		publicacionResponse.setNumeroPagina(publicaciones.getNumber());
		publicacionResponse.setMedidaPagina(publicaciones.getSize());
		publicacionResponse.setTotalElementos(publicaciones.getTotalElements());
		publicacionResponse.setTotalPaginas(publicaciones.getTotalPages());
		publicacionResponse.setUltima(publicaciones.isLast());
		
		return publicacionResponse;
	}

	

	@Override
	public PublicacionDto obtenerPublicacionPorId(long id) {
		//Buscar por ID
		Publicacion publicacion = pRep.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));
		return mapearDTO(publicacion);
	}

	@Override
	public PublicacionDto actualizarPublicacion(PublicacionDto publicacionDTO, long id) {
		//Buscar por ID
		Publicacion publicacion = pRep.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));
		
		publicacion.setTitulo(publicacionDTO.getTitulo());
		publicacion.setDescripcion(publicacionDTO.getDescripcion());
		publicacion.setContenido(publicacionDTO.getContenido());
		
		Publicacion publicacionActualizada = pRep.save(publicacion);

		return mapearDTO(publicacionActualizada);
	}

	@Override
	public void eliminarPublicacion(long id) {
		//Buscar por ID
		Publicacion publicacion = pRep.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));		
		
		pRep.delete(publicacion);
	}
	
	
	// Convierte entity a DTO
		private PublicacionDto mapearDTO(Publicacion publicacion) {
			PublicacionDto  publicacionDTO = mMap.map(publicacion, PublicacionDto.class);
	/*
	 * Para mapear una entidad a dto sin ModelMapper
	 
	 		PublicacionDto publicacionDTO = new PublicacionDto();
	 
			publicacionDTO.setId(publicacion.getId());
			publicacionDTO.setTitulo(publicacion.getTitulo());
			publicacionDTO.setDescripcion(publicacion.getDescripcion());
			publicacionDTO.setContenido(publicacion.getContenido());
	*/
			return publicacionDTO;

		}

		// Convierte de DTO a entity
		private Publicacion mapearEntity(PublicacionDto publicacionDTO) {
			Publicacion publicacion = mMap.map(publicacionDTO,Publicacion.class);
			/*
			 * Para mapear un DTO a entidad sin ModelMapper
			Publicacion publicacion = new Publicacion();

			publicacion.setTitulo(publicacionDTO.getTitulo());
			publicacion.setDescripcion(publicacionDTO.getDescripcion());
			publicacion.setContenido(publicacionDTO.getContenido());
			*/

			return publicacion;

		}
}
