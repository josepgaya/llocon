/**
 * 
 */
package com.josepgaya.llocon.core.exception;

/**
 * Excepció genèrica per als serveis.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class GenericServiceException extends RuntimeException {

	public GenericServiceException(
			String message) {
		super(message);
	}

	public GenericServiceException(
			String message,
			Throwable cause) {
		super(message, cause);
	}

}
