package fr.gouv.education.tribu.api.model;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public abstract class AbstractResponse {
	

	/** Contents */
	private final List<ContentDto> contents;
	
	/** Elapsed time */
	@ApiModelProperty(
			  value = "Temps d'exécution de l'appel (en millisecondes)",
			  example = "123")
	private Long elapsedTime;

	/** Message (in case of errors) */
	@ApiModelProperty(
			  value = "Message textuel en complément du code erreur",
			  example = "Erreur d'accès aux données")
	private String message;
	
	/** Code (in case of errors) */
	@ApiModelProperty(
			  value = "Code erreur",
			  example = "E03",
			  dataType = "ContentErrorCode")
	private String code;
	
	/** Ticket numbre (in case of errors) */
	@ApiModelProperty(
			  value = "Timestamp en complément du code erreur pour identification dans les logs")
	private String ticket;	
	

	public AbstractResponse(List<ContentDto> contents) {
		this.contents = contents;

	}	



	public List<ContentDto> getContents() {
		return contents;
	}

	public Long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	public String getTicket() {
		return ticket;
	}


	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	
}
