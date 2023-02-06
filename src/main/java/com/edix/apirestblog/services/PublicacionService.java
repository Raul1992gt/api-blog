package com.edix.apirestblog.services;


import com.edix.apirestblog.dto.PublicacionDto;
import com.edix.apirestblog.dto.PublicacionResponse;

public interface PublicacionService {
	
	public PublicacionDto crearPublicacion(PublicacionDto publicacionDTO);
	
	public PublicacionResponse obtenerLasPublicaciones(int numeroPagina, int medidaPagina, String ordenarPor, String sorDir);
	
	public PublicacionDto obtenerPublicacionPorId(long id);
	
	public PublicacionDto actualizarPublicacion(PublicacionDto publicacionDTO, long id);
	
	public void eliminarPublicacion(long id);

}
