package fr.gouv.education.tribu.api.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.gouv.education.tribu.api.service.ContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/directdownload")
@Api(value = "Download")
public class DownloadController extends AbstractWsController  {


	private static final String DD = "/directdownload";
	
	@Autowired
	private ContentService contentService;
	
	@ApiOperation(value = "Téléchargmeent direct")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 204, message = "Aucun résultat"), @ApiResponse(code = 400, message = "Erreur"),
			@ApiResponse(code = 403, message = "Non autorisé"), @ApiResponse(code = 400, message = "Erreur"),
			@ApiResponse(code = 500, message = "Erreur interne") })
	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	public  ResponseEntity<String> getFile(@PathVariable("uuid") String uuid, 
			@RequestParam(name = "token") String token,
		    HttpServletResponse response) {
		
		long startTime = System.currentTimeMillis();
		
		if(!contentService.checkToken(uuid, token)) {
			return logAndReturn(null, DD, token, startTime, ContentErrorCode.WARN_BAD_TOKEN);

		}
		
		return logAndReturn(null, DD, token, startTime, ContentErrorCode.INFO_OK);
	}
}
