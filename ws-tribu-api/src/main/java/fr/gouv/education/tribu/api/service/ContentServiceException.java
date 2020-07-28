package fr.gouv.education.tribu.api.service;

/**
 * Exception used for internal errors
 * 
 * @author Loïc Billon
 *
 */
public class ContentServiceException extends Exception {

	public ContentServiceException(String string, Exception e) {
		super(string, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -275261371229853217L;

}
