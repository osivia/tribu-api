package fr.gouv.education.tribu.api.repo;

/**
 * Exception used for nuxeo errors
 * 
 * @author Loïc Billon
 *
 */
public class RepositoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 311812819647233863L;
	
	public RepositoryException(String message) {
		super(message);
	}
	
	public RepositoryException(Exception cause) {
		super(cause);
	}
	
	public RepositoryException(String message, Exception cause) {
		super(message, cause);
	}

}
