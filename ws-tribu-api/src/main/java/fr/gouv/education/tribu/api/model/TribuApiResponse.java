package fr.gouv.education.tribu.api.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Global response object for documents
 * 
 * @author Loïc Billon
 *
 */
@ApiModel
public class TribuApiResponse extends AbstractResponse {
	
	@ApiModelProperty(
			  value = "Numéro de page courante",
			  example = "1")
	private Integer currentPageIndex;
	
	@ApiModelProperty(
			  value = "Nombre de pages total retournées par la requête",
			  example = "4")
	private Integer numberOfPages;
	
	@ApiModelProperty(
			  value = "Nombre de documents retournées par la requête",
			  example = "315")
	private Integer resultsCount;
	
	@ApiModelProperty(
			  value = "Taille des pages",
			  example = "25")
	private Integer pageSize;

	public TribuApiResponse(List<ContentDto> contents) {
		super(contents);
	}

	public Integer getCurrentPageIndex() {
		return currentPageIndex;
	}



	public void setCurrentPageIndex(Integer currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}



	public Integer getNumberOfPages() {
		return numberOfPages;
	}



	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}



	public Integer getResultsCount() {
		return resultsCount;
	}



	public void setResultsCount(Integer resultsCount) {
		this.resultsCount = resultsCount;
	}



	public Integer getPageSize() {
		return pageSize;
	}



	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


}
