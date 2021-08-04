package fr.gouv.education.tribu.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TribuApiQueryForm {

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
			  value = "Recherche par titre",
			  example = "Rapport d'activité")
	private String title;
	
	@ApiModelProperty(
			  value = "Numéro de page",
			  example = "2")
	private Integer pageNumber;
	
	@ApiModelProperty(
			  value = "Nombre de résultats par  page",
			  example = "25")
	private Integer pageSize;
	
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
