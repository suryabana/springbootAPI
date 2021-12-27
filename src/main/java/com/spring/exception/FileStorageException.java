package com.spring.exception;

public class FileStorageException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1007406207269762956L;

	public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}