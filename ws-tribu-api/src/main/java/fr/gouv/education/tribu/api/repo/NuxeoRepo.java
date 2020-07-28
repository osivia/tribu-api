package fr.gouv.education.tribu.api.repo;

/**
 * Access layer to nuxeo
 * 
 * @author Loïc Billon
 *
 */
public interface NuxeoRepo {


	/**
	 * Run an automation command
	 * 
	 * @param nuxeoSession
	 * @param command
	 * @return
	 * @throws RepositoryException
	 */
	Object executeCommand(String userId, NuxeoCommand command) throws RepositoryException;

}
