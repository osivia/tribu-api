package fr.gouv.education.tribu.api.controller;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.gouv.education.tribu.api.model.DownloadForm;
import fr.gouv.education.tribu.api.model.DownloadUrlResponse;
import fr.gouv.education.tribu.api.model.SearchForm;
import fr.gouv.education.tribu.api.model.TribuApiResponse;
import fr.gouv.education.tribu.api.repo.RepositoryException;
import fr.gouv.education.tribu.api.service.ContentService;
import fr.gouv.education.tribu.api.service.ContentServiceException;
import fr.gouv.education.tribu.api.service.UserNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Main controller for spaces content
 * 
 * @author Loïc Billon
 *
 */
@RestController
@RequestMapping("/contents")
@Api(value = "Contents")
public class SearchController extends AbstractWsController {

	protected static final String CONTEXTE = "/contents";
	private static final String SEARCH = CONTEXTE + "/search";
	private static final String DOWNLOAD = CONTEXTE + "/download";

	
	@Autowired
	private ContentService contentService;

	
	@ApiOperation(value = "Recherche de documents", response = TribuApiResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Recherche ok"),
			@ApiResponse(code = 204, message = "Aucun résultat"), @ApiResponse(code = 400, message = "Erreur"),
			@ApiResponse(code = 403, message = "Non autorisé"), @ApiResponse(code = 400, message = "Erreur"),
			@ApiResponse(code = 500, message = "Erreur interne") })
	@PostMapping(value = "/search/", consumes = { "application/json" }, produces = "application/json")
	public ResponseEntity<String> search(@RequestBody SearchForm search) {

		long startTime = System.currentTimeMillis();

		if (search == null) {
			return logAndReturn(null, SEARCH, "?", startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "search");
		}
		else if (StringUtils.isBlank(search.getUser()) || StringUtils.isBlank(search.getAppId())) {
			return logAndReturn(null, SEARCH, "?", startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "user et appId");
		}
		else if (search.getTitle() == null && search.getFulltext() == null) {
			return logAndReturn(null, SEARCH, search.getUser(), startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "titre ou fulltext");
		
		} 
		else {

			String logUser = search.getAppId() + "/" + search.getUser();
			
			TribuApiResponse liste;
			try {

				liste = contentService.search(search);

			} catch (UserNotFoundException e) {

				return logStackAndReturn(e, SEARCH, logUser, startTime, ContentErrorCode.WARN_WRONG_USER);
			} catch (RepositoryException e) {
				return logStackAndReturn(e, SEARCH, logUser, startTime, ContentErrorCode.ERROR_BACKEND);
			} 
			catch (ContentServiceException e) {

				return logStackAndReturn(e, SEARCH, logUser, startTime, ContentErrorCode.ERROR_TECH);
			}

			return logAndReturn(liste, SEARCH, logUser, startTime, ContentErrorCode.INFO_OK);

		}

	}
	
	
	@ApiOperation(value = "Demande téléchargement de documents", response = DownloadUrlResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Recherche ok"),
			@ApiResponse(code = 403, message = "Non autorisé"), 
			@ApiResponse(code = 404, message = "Document non trouvé"), @ApiResponse(code = 400, message = "Erreur"),@ApiResponse(code = 400, message = "Erreur"),
			@ApiResponse(code = 500, message = "Erreur interne") })
	@PostMapping(value = "/download/", consumes = { "application/json" }, produces = "application/json")
	public ResponseEntity<String> download(@RequestBody DownloadForm dlForm) {

		long startTime = System.currentTimeMillis();

		if (dlForm == null) {
			return logAndReturn(null, DOWNLOAD, "?", startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "search");
		}
		else if (StringUtils.isBlank(dlForm.getUser()) || StringUtils.isBlank(dlForm.getAppId())) {
			return logAndReturn(null, SEARCH, "?", startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "user et appId");
		}	
		else if (dlForm.getUuid() == null && dlForm.getUuid() == null) {
			return logAndReturn(null, DOWNLOAD, dlForm.getUser(), startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "uuid");
		
		} 
		else {


			String logUser = dlForm.getAppId() + "/" + dlForm.getUser();
			
			DownloadUrlResponse url;
			try {

				url = contentService.download(dlForm);
				
				// let ehache synchronize the token to peers
				TimeUnit.SECONDS.sleep(1);

			} catch (UserNotFoundException e) {

				return logStackAndReturn(e, SEARCH, logUser, startTime, ContentErrorCode.WARN_WRONG_USER);
			} catch (RepositoryException e) {

				return logStackAndReturn(e, DOWNLOAD, logUser, startTime, ContentErrorCode.ERROR_BACKEND);
			} catch (ContentServiceException e) {

				return logStackAndReturn(e, DOWNLOAD, logUser, startTime, ContentErrorCode.ERROR_TECH);
			} catch (InterruptedException e) {

				return logStackAndReturn(e, DOWNLOAD, logUser, startTime, ContentErrorCode.ERROR_TECH);
			}

			return logAndReturn(url, DOWNLOAD, logUser, startTime, ContentErrorCode.INFO_OK);	

			

		}

	}	


}
