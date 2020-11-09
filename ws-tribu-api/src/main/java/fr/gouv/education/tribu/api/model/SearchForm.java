package fr.gouv.education.tribu.api.model;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SearchForm extends TribuApiQueryForm {

	@ApiModelProperty(
			  value = "recherche fulltext",
			  example = "contenu")
	private String fulltext;
	
	@ApiModelProperty(
			  value = "recherche les documents dont la date est postérieure à",
			  example = "2020-07-24T12:37:56.983Z")
	private Date modificationBeginDate;
	
	@ApiModelProperty(
			  value = "recherche les documents dont la date est antérieure à",
			  example = "2020-07-24T12:37:56.983Z")
	private Date modificationEndDate;
	
	@ApiModelProperty(
			  value = "tri par pertinence ou par date de modification",
			  example = "relevance ou last-modification")
	private SearchSort sort;

	@ApiModelProperty(
			  value = "inveser le tri pour le tri par date")
	private Boolean reversedSort;


	public SearchForm() {
		super();
	}


	public String getFulltext() {
		return fulltext;
	}

	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}

	public Date getModificationBeginDate() {
		return modificationBeginDate;
	}

	public void setModificationBeginDate(Date modificationBeginDate) {
		this.modificationBeginDate = modificationBeginDate;
	}

	public Date getModificationEndDate() {
		return modificationEndDate;
	}

	public void setModificationEndDate(Date modificationEndDate) {
		this.modificationEndDate = modificationEndDate;
	}

	public SearchSort getSort() {
		return sort;
	}

	public void setSort(SearchSort sort) {
		this.sort = sort;
	}

	public Boolean getReversedSort() {
		return reversedSort;
	}

	public void setReversedSort(Boolean reversedSort) {
		this.reversedSort = reversedSort;
	}
}
