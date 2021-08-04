package fr.gouv.education.tribu.api.controller;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * Enum of webservices codes
 * 
 * @author Loïc Billon
 *
 */
public enum ContentErrorCode {

	/** results */
	INFO_OK(LogLevel.INFO, "I01", null, HttpStatus.OK),
	
	/** no results */
	WARN_NO_DATA(LogLevel.WARN, "W01", "Aucun document trouvé", HttpStatus.NO_CONTENT),
	
	/** bad parameters */
	WARN_WRONG_PARAMETER(LogLevel.WARN, "W02","%s est requis", HttpStatus.BAD_REQUEST),
	
	/** bad apiKey */
	WARN_APIKEY(LogLevel.WARN, "W03","Clé API incorrecte", HttpStatus.FORBIDDEN),
	
	/** missing apiKey */
	WARN_MISSING_APIKEY(LogLevel.WARN, "W04","Header clé API manquant", HttpStatus.FORBIDDEN),
	
	/** bad token */
	WARN_BAD_TOKEN(LogLevel.WARN, "W05","Demande téléchargement direct incorrcte ou expirée", HttpStatus.FORBIDDEN),
	
	/** wrong user */
	WARN_WRONG_USER(LogLevel.WARN, "W06","Utilisateur non trouvé", HttpStatus.FORBIDDEN),	
	
	/** error with repository */
	ERROR_BACKEND(LogLevel.ERROR, "E01","Erreur d'accès aux données", HttpStatus.INTERNAL_SERVER_ERROR),
	
	/** internal error */
	ERROR_TECH(LogLevel.ERROR, "E02","Erreur interne", HttpStatus.INTERNAL_SERVER_ERROR);
	

	private final LogLevel level;
	private final String message;
	private final String code;
	private final HttpStatus status;

	private ContentErrorCode(LogLevel level, String code, String message, HttpStatus status) {
		this.level = level;
		this.code = code;
		this.message = message;
		this.status = status;
		
	}

	
	public LogLevel getLevel() {
		return level;
	}


	public String getMessage() {
		return message;
	}


	public String getCode() {
		return code;
	}


	public HttpStatus getStatus() {
		return status;
	}

	
	
}
