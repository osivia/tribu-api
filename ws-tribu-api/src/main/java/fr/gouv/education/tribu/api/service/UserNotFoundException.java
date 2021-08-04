package fr.gouv.education.tribu.api.service;

public class UserNotFoundException extends Exception {

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
