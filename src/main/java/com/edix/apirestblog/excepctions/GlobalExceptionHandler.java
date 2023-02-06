package com.edix.apirestblog.excepctions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.edix.apirestblog.dto.ErrorDetails;

// Esta anotación le dice que a esta clase le va a permitir controlar las excepciones de la app
@ControllerAdvice
//Extendemos de esta clase, para usar el método cuando no esta valido
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> manejarResourcesNotFoundException(ResourceNotFoundException exception, WebRequest webRequest){
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
		
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BlogAppException.class)
	public ResponseEntity<ErrorDetails> blogAppException(BlogAppException exception, WebRequest webRequest){
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
		
		return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> manejarGlobalException(Exception exception, WebRequest webRequest){
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
		
		return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	//Método para manejar los errores
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errores = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach((error)-> {
			//Encontramos el campo donde tenemos el error
			String nombreCampo = ((FieldError)error).getField();
			//Obtenemos el mensaje 
			String mensaje = error.getDefaultMessage();
			
			errores.put(nombreCampo, mensaje);
		});
		
		return new ResponseEntity<>(errores,HttpStatus.BAD_REQUEST);
	}
	
	
}
