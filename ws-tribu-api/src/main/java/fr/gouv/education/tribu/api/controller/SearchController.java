package fr.gouv.education.tribu.api.controller;

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
//
//	private static final String THUMBNAIL = "/thumbnail";

	private static final String SEARCH = "/search";
	private static final String DOWNLOAD = "/download";
//
//	private static final String SPONSORLINKS = "/sponsorlinks";
//
//	private static final String SHAREDWITHME = "/sharedwithme";
//
//	private static final String PORTALSITES = "/portalsites";
//
//	private static final String TRISKELL = "/triskell";
//
//	private static final String WORKSPACES = "/workspaces";
//
//	private static final String USERWORKSPACES = "/userworkspaces";
	
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
		else if (search.getUser() == null) {
			return logAndReturn(null, SEARCH, "?", startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "user");
		}
		else if (search.getAppId() == null) {
			return logAndReturn(null, SEARCH, search.getUser(), startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "appId");
		}		
		else if (search.getTitle() == null && search.getFulltext() == null) {
			return logAndReturn(null, SEARCH, search.getUser(), startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "query by title or fulltext");
		
		} 
		else {

			String logUser = search.getAppId() + "/" + search.getUser();
			
			TribuApiResponse liste;
			try {

				liste = contentService.search(search);

			} catch (RepositoryException e) {

				return logStackAndReturn(e, SEARCH, logUser, startTime, ContentErrorCode.ERROR_BACKEND);
			} catch (ContentServiceException e) {

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
		else if (dlForm.getUser() == null) {
			return logAndReturn(null, DOWNLOAD, "?", startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "user");
		}
		else if (dlForm.getAppId() == null) {
			return logAndReturn(null, DOWNLOAD, dlForm.getUser(), startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "appId");
		}				
		else if (dlForm.getUuid() == null && dlForm.getUuid() == null) {
			return logAndReturn(null, DOWNLOAD, dlForm.getUser(), startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "uuid");
		
		} 
		else {


			String logUser = dlForm.getAppId() + "/" + dlForm.getUser();
			
			DownloadUrlResponse url;
			try {

				url = contentService.download(dlForm);
				
				

			} catch (RepositoryException e) {

				return logStackAndReturn(e, DOWNLOAD, logUser, startTime, ContentErrorCode.ERROR_BACKEND);
			} catch (ContentServiceException e) {

				return logStackAndReturn(e, DOWNLOAD, logUser, startTime, ContentErrorCode.ERROR_TECH);
			}

			return logAndReturn(url, DOWNLOAD, logUser, startTime, ContentErrorCode.INFO_OK);	

			

		}

	}	
	
//	/**
//	 * Get thumbnail
//	 * 
//	 * @param request
//	 * @param response
//	 * @param user
//	 * @param principal
//	 * @return
//	 */
//	@ApiOperation(value = "Récupère une vignette")
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Recherche ok"),
//			@ApiResponse(code = 204, message = "Aucun résultat"), @ApiResponse(code = 400, message = "Erreur"),
//			@ApiResponse(code = 500, message = "Erreur interne") })
//	@GetMapping(value = "/{user}/{docUuid}/thumbnail", produces = "image/jpeg")
//	public byte[] getUserWorkspaces(HttpServletRequest request, HttpServletResponse response,
//			@ApiParam(value = "Utilisateur", required = true) @PathVariable String user, 
//			@ApiParam(value = "Document", required = true) @PathVariable String docUuid,
//			Principal principal) {
//
//		long startTime = System.currentTimeMillis();
//		
//		if (user == null) {
//			logAndReturn(null, THUMBNAIL, "?", startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "user");
//			return null;
//
//		} else if (docUuid == null) {
//			logAndReturn(null, THUMBNAIL, "?", startTime, ContentErrorCode.WARN_WRONG_PARAMETER, "docUuid");
//			return null;	
//
//		} else {
//				
//			byte[] thumbnail;
//			try {
//				thumbnail = contentService.getThumbnail(user, docUuid);
//			} catch (RepositoryException e) {
//				logStackAndReturn(e, THUMBNAIL, user, startTime, ContentErrorCode.ERROR_BACKEND);
//				return null;	
//
//
//			} catch (ContentServiceException e) {
//				logStackAndReturn(e, THUMBNAIL, user, startTime, ContentErrorCode.ERROR_TECH);
//				return null;	
//			}
//			
//			
//			return thumbnail;
//			
//		}
//		
//	}

}
