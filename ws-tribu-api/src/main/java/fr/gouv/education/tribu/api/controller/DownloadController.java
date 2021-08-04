package fr.gouv.education.tribu.api.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.gouv.education.tribu.api.model.BinaryContent;
import fr.gouv.education.tribu.api.repo.RepositoryException;
import fr.gouv.education.tribu.api.service.ContentService;
import fr.gouv.education.tribu.api.service.UserNotFoundException;
import fr.gouv.education.tribu.api.service.token.DownloadToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/directdownload")
@Api(value = "Download")
public class DownloadController extends AbstractWsController {

	private static final String DD = "/directdownload";


	@Autowired
	private ContentService contentService;

	@ApiOperation(value = "Téléchargmeent direct")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 204, message = "Aucun résultat"), @ApiResponse(code = 400, message = "Erreur"),
			@ApiResponse(code = 403, message = "Non autorisé"), @ApiResponse(code = 400, message = "Erreur"),
			@ApiResponse(code = 500, message = "Erreur interne") })
	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<String> getFile(@PathVariable("uuid") String uuid, @RequestParam(name = "token") String token,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		long startTime = System.currentTimeMillis();

		DownloadToken tokenObject = contentService.checkToken(uuid, token);

		if (token == null || tokenObject == null) {

			// TODO gestion retour
			return logAndReturn(null, DD, token, startTime, ContentErrorCode.WARN_BAD_TOKEN);

		}

		OutputStream output = response.getOutputStream();
		String userId = tokenObject.getUser();

		try {
			BinaryContent content = contentService.startDownload(uuid, userId);

			// Last modified
			long lastModified = System.currentTimeMillis();

			response.setContentType(content.getMimeType());

			response.setHeader("Content-Disposition", "attachment; filename=\"" + content.getFileName() + "\"");

			response.setHeader("Content-Length", String.valueOf(content.getCount()));

			response.setDateHeader("Last-Modified", lastModified);

			InputStream in = new FileInputStream(content.getTempFile());
			BufferedOutputStream out = new BufferedOutputStream(output, 4096);
			try {
				byte[] b = new byte[4096];
				int i = -1;
				while ((i = in.read(b)) != -1) {
					out.write(b, 0, i);
				}
				out.flush();
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}

		} 
		 catch (UserNotFoundException e) {
				return logAndReturn(null, DD, tokenObject.getUser() + "@" + tokenObject.getAppId(), startTime,
						ContentErrorCode.WARN_WRONG_USER);

			}
		catch (RepositoryException e) {
			return logAndReturn(null, DD, tokenObject.getUser() + "@" + tokenObject.getAppId(), startTime,
					ContentErrorCode.ERROR_BACKEND);

		}

		return logAndReturn(null, DD, tokenObject.getUser() + "@" + tokenObject.getAppId(), startTime,
				ContentErrorCode.INFO_OK);

	}

}
