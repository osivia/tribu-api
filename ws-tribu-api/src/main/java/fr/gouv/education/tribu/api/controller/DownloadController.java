package fr.gouv.education.tribu.api.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.gouv.education.tribu.api.controller.binaries.BytesRange;
import fr.gouv.education.tribu.api.controller.binaries.ConstrainedInputStream;
import fr.gouv.education.tribu.api.model.BinaryContent;
import fr.gouv.education.tribu.api.repo.RepositoryException;
import fr.gouv.education.tribu.api.service.ContentService;
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

	/** Multipart boundary. */
	private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

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
		String appId = tokenObject.getAppId();

		try {
			BinaryContent content = contentService.startDownload(uuid, appId);

			// TODO streaming
//				this.stream(request, response, content, output);

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

		} catch (RepositoryException e) {
			return logAndReturn(null, DD, tokenObject.getUser() + "@" + tokenObject.getAppId(), startTime,
					ContentErrorCode.ERROR_BACKEND);

		}

		return logAndReturn(null, DD, tokenObject.getUser() + "@" + tokenObject.getAppId(), startTime,
				ContentErrorCode.INFO_OK);

	}

	private void stream(HttpServletRequest request, HttpServletResponse response, BinaryContent content,
			OutputStream output) throws IOException {
		// Length
		long length = content.getCount();
		// Last modified
		long lastModified = System.currentTimeMillis();
		// Expires
//        long expires = lastModified + BINARY_TIMEOUT;
		// Content type
		String contentType = content.getMimeType();
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		// Content disposition
		response.setHeader("Content-Disposition", "attachment; filename=\"" + content.getFileName() + "\"");

		// Validate request headers for resume : If-Unmodified-Since header should be
		// greater than LastModified.
		// If not, then return 412.
		long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
		if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			return;
		}

		// Prepare some variables.
		// The full Range represents the complete file.
		BytesRange full = new BytesRange(0, length - 1, length);
		List<BytesRange> ranges = new ArrayList<BytesRange>();

		// Validate and process Range and If-Range headers.
		String range = request.getHeader("Range");
		if (range != null) {
			// Range header should match format "bytes=n-n,n-n,n-n...". If not, then return
			// 416.
			if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
				response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
				response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
				return;
			}

			// If any valid If-Range header, then process each part of byte range.
			if (ranges.isEmpty()) {
				for (String part : range.substring(6).split(",")) {
					// Assuming a file with length of 100, the following examples returns bytes at:
					// 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
					NumberUtils.toInt(StringUtils.substringBefore(part, "-"), -1);

					long start = NumberUtils.toInt(StringUtils.substringBefore(part, "-"), -1);
					long end = NumberUtils.toInt(StringUtils.substringAfter(part, "-"), -1);

					if (start == -1) {
						start = length - end;
						end = length - 1;
					} else if (end == -1 || end > length - 1) {
						end = length - 1;
					}

					// Check if Range is syntactically valid. If not, then return 416.
					if (start > end) {
						response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
						response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
						return;
					}

					// Add range.
					ranges.add(new BytesRange(start, end, length));
				}
			}
		}

		// Initialize response.
		response.reset();
		response.setBufferSize(8192);
		// response.setHeader("Content-Disposition", disposition);
		response.setDateHeader("Last-Modified", lastModified);
		// response.setDateHeader("Expires", expires);

		if (range == null) {
			response.setContentType(contentType);
			response.setHeader("Content-Length", String.valueOf(content.getCount()));

			// Copy.
			copy(content, output, 0, content.getCount());
		} else {
			response.setHeader("Accept-Ranges", "bytes");
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

			// Send requested file (part(s)) to client
			if (ranges.isEmpty() || full.equals(ranges.get(0))) {

				// Return full file.
				BytesRange r = full;
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + r.getStart() + "-" + r.getEnd() + "/" + r.getTotal());

				// Content length is not directly predictable in case of GZIP.
				// So only add it if there is no means of GZIP, else browser will hang.
				response.setHeader("Content-Length", String.valueOf(r.getLength()));

				// Copy full range.
				copy(content, output, r.getStart(), r.getLength());

			} else if (ranges.size() == 1) {

				// Return single part of file.
				BytesRange r = ranges.get(0);
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + r.getStart() + "-" + r.getEnd() + "/" + r.getTotal());
				response.setHeader("Content-Length", String.valueOf(r.getLength()));

				// Copy single part range.
				copy(content, output, r.getStart(), r.getLength());

			} else {

				// Return multiple parts of file.
				response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);

				// Cast back to ServletOutputStream to get the easy println methods.
				ServletOutputStream sos = (ServletOutputStream) output;

				// Copy multi part range.
				for (BytesRange r : ranges) {
					// Add multipart boundary and header fields for every range.
					sos.println();
					sos.println("--" + MULTIPART_BOUNDARY);
					sos.println("Content-Type: " + contentType);
					sos.println("Content-Range: bytes " + r.getStart() + "-" + r.getEnd() + "/" + r.getTotal());

					// Copy single part range of multi part range.
					copy(content, output, r.getStart(), r.getLength());
				}

				// End with multipart boundary.
				sos.println();
				sos.println("--" + MULTIPART_BOUNDARY + "--");
			}
		}
	}

	private void copy(BinaryContent content, OutputStream output, long start, long length) throws IOException {
		InputStream input = content.getStream();
		try {
			byte[] buffer = new byte[8192];
			int read;

			if ((start > 0) || (length != content.getCount())) {
				// Write partial range
				input = new ConstrainedInputStream(input, length);
				input.skip(start);
			}

			while ((read = input.read(buffer)) > 0) {
				output.write(buffer, 0, read);
			}
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
}
