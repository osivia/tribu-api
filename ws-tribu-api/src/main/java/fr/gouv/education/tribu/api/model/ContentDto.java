package fr.gouv.education.tribu.api.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.gouv.education.tribu.api.service.ContentServiceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Dto class for nuxeo documents response
 * 
 * @author Loïc Billon
 *
 */
@Component(value = ContentDto.COMPONENT_NAME)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ApiModel
public class ContentDto {
	
	public static final String COMPONENT_NAME = "ContentDto";
	
	@Value("${portal.url}")
	private String portalUrl;
	
	private static final String PORTAL_CMS_PATH = "/cms";
	
	/** Doc uuid */
	@ApiModelProperty(
			  value = "Identifiant nuxeo",
			  example = "a5fcbab0-6069-4bcf-813f-08c56f48a0ea")
	private String uuid;
	
	/** Doc title */
	
	@ApiModelProperty(
			  value = "Titre du document",
			  example = "Rapport d'activité")
	private String title;
	
	/** Description */
	@ApiModelProperty(
			  value = "Description du document",
			  example = "Rapport d'activité de la réunion du 16-07")
	private String description;
	
	/** Creator */
	@ApiModelProperty(
			  value = "Auteur du document",
			  example = "Jacques Dupont")
	public String creator;
	
	/** Modified */
	
	@ApiModelProperty(
			  value = "Date de modification",
			  example = "2020-07-24T12:37:56.983Z")
	public Date modified;
	
	/** Created */
	@ApiModelProperty(
			  value = "Date de création",
			  example = "2020-07-24T12:37:56.983Z")
	public Date created;
	
	/** last contributor */
	@ApiModelProperty(
			  value = "Dernier contributeur sur le document",
			  example = "Jacques Dupont")
	public String lastContributor;
	
	/** issued */
	@ApiModelProperty(
			  value = "Date de publication",
			  example = "2020-07-24T12:37:56.983Z")
	public Date issued;
	
	/** space title */
	@ApiModelProperty(
			  value = "Titre de l'espace contenant le document",
			  example = "Espace de modernisation")
	public String spaceTitle;
	
	/** Portal URL */
	@ApiModelProperty(
			  value = "Permalien vers tribu",
			  example = "https://tribu.phm.education.gouv.fr/portal/share/guide-tribu")
	private String url;
	
	/** Portal URL */
	@ApiModelProperty(
			  value = "Propriétés nuxeo du document")
	private Map<String, Object> properties = new LinkedHashMap<>();
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getLastContributor() {
		return lastContributor;
	}

	public void setLastContributor(String lastContributor) {
		this.lastContributor = lastContributor;
	}

	public Date getIssued() {
		return issued;
	}

	public void setIssued(Date issued) {
		this.issued = issued;
	}

	public String getSpaceTitle() {
		return spaceTitle;
	}

	public void setSpaceTitle(String spaceTitle) {
		this.spaceTitle = spaceTitle;
	}
	
	

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public static String getComponentName() {
		return COMPONENT_NAME;
	}

	public static String getPortalCmsPath() {
		return PORTAL_CMS_PATH;
	}

	public ContentDto toDto(Document document) throws ContentServiceException {
		
		this.setTitle(document.getTitle());
		this.setUuid(document.getId());
		
		this.setCreated(document.getProperties().getDate("dc:created"));
		this.setCreator(document.getProperties().getString("dc:creator"));
		this.setDescription(document.getProperties().getString("dc:description"));
		this.setIssued(document.getDate("dc:issued"));
		this.setLastContributor(document.getProperties().getString("dc:lastContributor"));
		this.setModified(document.getProperties().getDate("dc:modified"));
		this.setSpaceTitle(document.getProperties().getString("ttc:spaceTitle"));
		
		this.properties = document.getProperties().map();
		
		
		//this.setThumbnail(toThumbnailUrl(document));
		
		String path = document.getPath();
		

		URL url;
		try {
			url = new URL(portalUrl + PORTAL_CMS_PATH + path);
		} catch (MalformedURLException e) {
			throw new ContentServiceException("malformed portal URL", e);
		}
		this.setUrl(url.toString());
		
		return this;
		
	}


}
