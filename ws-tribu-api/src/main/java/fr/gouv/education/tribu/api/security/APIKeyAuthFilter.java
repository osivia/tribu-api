package fr.gouv.education.tribu.api.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import fr.gouv.education.tribu.api.controller.ContentErrorCode;

public class APIKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {


	protected static final Log LOGGER = LogFactory.getLog("tribu.api");
	
    private String principalRequestHeader;
	
    
    public APIKeyAuthFilter(String principalRequestHeader) {
		this.principalRequestHeader = principalRequestHeader;
    	
    }
	
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String header = request.getHeader(principalRequestHeader);
		
		if(header == null) {
			LOGGER.warn(ContentErrorCode.WARN_MISSING_APIKEY.getCode() + " " + ContentErrorCode.WARN_MISSING_APIKEY.getMessage());

		}
		
		return header;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return "N/A";	
	} 

		
}
