package fr.gouv.education.tribu.api.repo;

import org.nuxeo.ecm.automation.client.Session;

/**
 * Global interface of a nuxeo command
 * 
 * @author Loïc Billon
 *
 */
public interface NuxeoCommand {

	/**
	 * Execute a nuxeo command
	 * 
	 * @param nuxeoSession
	 * @return
	 * @throws RepositoryException
	 */
	Object execute(Session nuxeoSession) throws RepositoryException;

	/**
	 * Get an id for command (for logging)
	 * 
	 * @return
	 */
	String getId();

}
