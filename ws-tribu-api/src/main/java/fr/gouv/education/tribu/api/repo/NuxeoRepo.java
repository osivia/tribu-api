package fr.gouv.education.tribu.api.repo;

import fr.gouv.education.tribu.api.service.UserNotFoundException;

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
	 * @throws UserNotFoundException 
	 */
	Object executeCommand(String userId, NuxeoCommand command) throws RepositoryException, UserNotFoundException;

}
