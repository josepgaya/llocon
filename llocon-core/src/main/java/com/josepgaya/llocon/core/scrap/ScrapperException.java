/**
 * 
 */
package com.josepgaya.llocon.core.scrap;

/**
 * Excepció per a l'scrapper.
 * 
 * @author josepgaya
 */
public class ScrapperException extends RuntimeException {

	public ScrapperException() {
		super();
	}
	public ScrapperException(String message) {
		super(message);
	}
	public ScrapperException(String message, Throwable cause) {
		super(message, cause);
	}
	public ScrapperException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 4928796263230796345L;

}
