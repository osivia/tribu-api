package fr.gouv.education.tribu.api.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.gouv.education.tribu.api.service.ContentServiceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ApiModel
public class DownloadUrlResponse extends AbstractResponse {

	
	public DownloadUrlResponse(List<ContentDto> contents) {
		super(contents);
	}

	private static final String SERVLET_PATH = "/directdownload/";

	@Value("${api.url}")
	private String apiUrl;
	
	@ApiModelProperty(
			  value = "URL de téléchargement éphémère, avec paramètre du jeton",
			  example = "https://tribu-api.in.phm.education.gouv.fr/tribu-api/directdownload/a5fcbab0-6069-4bcf-813f-08c56f48a0ea?token=123456vUsfkok")
	private String directDownloadUrl;

	public String getDirectDownloadUrl() {
		return directDownloadUrl;
	}

	public void setDirectDownloadUrl(String directDownloadUrl) {
		this.directDownloadUrl = directDownloadUrl;
	}

	public DownloadUrlResponse toResponse() throws ContentServiceException {
		
		
		byte[] array = new byte[64]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));
	    
	    
		try {


			UUID uuid = UUID.randomUUID();
			
			URL url = new URL(apiUrl + SERVLET_PATH + getContents().get(0).getUuid() + "?token=" + uuid);
			this.directDownloadUrl = url.toString();
			return this;
		} catch (MalformedURLException e) {
			throw new ContentServiceException("malformed url exception", e);
		}
		
	}
	
	
}
