package fr.gouv.education.tribu.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DownloadForm {

	@ApiModelProperty(
			  value = "Identifiant appli pour autoriser le webervice à communiquer avec nuxeo",
			  example = "MF2")
	private String appId;
	
	@ApiModelProperty(
			  value = "Identifiant utilisateur depuis l'application d'origine",
			  example = "jean.dupont@education.fr")
	private String user;
	
	
	@ApiModelProperty(
			  value = "Hash numen transmis par la FIM",
			  example = "70390851d88c7bfab1ce6bfc3bef4e9327a5e85447f3519d4704372362319d9d")
	private String hashnumen;	
	
	@ApiModelProperty(
			  value = "Identifiant nuxeo",
			  example = "a5fcbab0-6069-4bcf-813f-08c56f48a0ea")
	private String uuid;
	
	@ApiModelProperty(
			  value = "Fournit le document d'origine ou un aperçu pdf")
	private Boolean anyToPdf = Boolean.FALSE;

	
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getHashnumen() {
		return hashnumen;
	}

	public void setHashnumen(String hashnumen) {
		this.hashnumen = hashnumen;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Boolean getAnyToPdf() {
		return anyToPdf;
	}

	public void setAnyToPdf(Boolean anyToPdf) {
		this.anyToPdf = anyToPdf;
	}
	
	
}
