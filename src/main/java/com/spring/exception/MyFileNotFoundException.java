package com.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8773413977650001539L;

	public MyFileNotFoundException(String message) {
        super(message);
    }

    public MyFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

	public MyFileNotFoundException(MyFileNotFoundException ex) {
		// TODO Auto-generated constructor stub
	}
}