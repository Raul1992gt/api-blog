package com.edix.apirestblog.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ComentarioDto {

	private long id;
	
	@NotEmpty(message = "El nombre no puede estar vacio")
	private String nombre;
	
	
	@NotEmpty(message = "El email no puede estar vacío")
	@Email
	private String email;
	
	@NotEmpty
	@Size(min=10,message = "El cuerpo del comentario debe tener al menos 10 caracteres")
	private String cuerpo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public ComentarioDto() {
		super();
	}
	
	
}
