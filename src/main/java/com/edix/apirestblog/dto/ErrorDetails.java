package com.edix.apirestblog.dto;

import java.util.Date;

public class ErrorDetails {

	private Date marcaDeTiempo;
	private String mensaje;
	private String details;

	public ErrorDetails(Date marcaDeTiempo, String mensaje, String details) {
		super();
		this.marcaDeTiempo = marcaDeTiempo;
		this.mensaje = mensaje;
		this.details = details;
	}

	public Date getMarcaDeTiempo() {
		return marcaDeTiempo;
	}

	public void setMarcaDeTiempo(Date marcaDeTiempo) {
		this.marcaDeTiempo = marcaDeTiempo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	

}
