package fr.gouv.education.tribu.api.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
	
	@ApiModelProperty(
			  value = "Jeton de téléchargement contenu dans l'url",
			  example = "123456vUsfkok")
	private String token;

	public String getDirectDownloadUrl() {
		return directDownloadUrl;
	}

	public String getToken() {
		return token;
	}

	public DownloadUrlResponse toResponse(String token) throws ContentServiceException {

	    	    
		try {
			this.token = token;
			
			URL url = new URL(apiUrl + SERVLET_PATH + getContents().get(0).getUuid() + "?token=" + token);
			this.directDownloadUrl = url.toString();
			return this;
		} catch (MalformedURLException e) {
			throw new ContentServiceException("malformed url exception", e);
		}
		
	}
	
	
}
