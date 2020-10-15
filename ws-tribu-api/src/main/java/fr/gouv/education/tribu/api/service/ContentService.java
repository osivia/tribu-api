package fr.gouv.education.tribu.api.service;

import fr.gouv.education.tribu.api.model.BinaryContent;
import fr.gouv.education.tribu.api.model.DownloadForm;
import fr.gouv.education.tribu.api.model.DownloadUrlResponse;
import fr.gouv.education.tribu.api.model.SearchForm;
import fr.gouv.education.tribu.api.model.TribuApiResponse;
import fr.gouv.education.tribu.api.repo.RepositoryException;
import fr.gouv.education.tribu.api.service.token.DownloadToken;

/**
 * 
 * @author Loïc Billon
 *
 */
public interface ContentService {

	/**
	 * Search documents
	 * 
	 * @param search
	 * @return
	 */
	TribuApiResponse search(SearchForm search) throws RepositoryException, ContentServiceException;

	/**
	 * Download document (ask for a token url)
	 * 
	 * @param dlForm
	 * @return
	 * @throws ContentServiceException 
	 * @throws RepositoryException 
	 */
	DownloadUrlResponse download(DownloadForm dlForm) throws RepositoryException, ContentServiceException;

	/**
	 * Check if the token given in url is valid
	 * 
	 * @param docUuid
	 * @param token
	 * @return
	 */
	DownloadToken checkToken(String docUuid, String token);

	/**
	 * 
	 * 
	 * @param uuid
	 * @param appId
	 * @return
	 */
	BinaryContent startDownload(String uuid, String appId) throws RepositoryException;


}