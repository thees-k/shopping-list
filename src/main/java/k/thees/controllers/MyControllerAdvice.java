package k.thees.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException exception){

		List<Map<String, String>> errorList = exception.getFieldErrors().stream().map(this::convertFieldErrorToMap).toList();
		return ResponseEntity.badRequest().body(errorList);
	}

	private Map<String, String> convertFieldErrorToMap(FieldError fieldError){
		Map<String, String> map = new HashMap<>();
		map.put(fieldError.getField(), fieldError.getDefaultMessage());
		return map;
	}
}
