package k.thees.shoppinglist.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value Not Found")
public class MyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
