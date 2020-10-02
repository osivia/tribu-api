package fr.gouv.education.tribu.api.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import fr.gouv.education.tribu.api.controller.ContentErrorCode;

public class APIKeyAuthenticationManager implements AuthenticationManager {

	protected static final Log LOGGER = LogFactory.getLog("tribu.api");
	
    private String principalRequestValue;
    
    public APIKeyAuthenticationManager(String principalRequestValue) {
		this.principalRequestValue = principalRequestValue;
    	
    }
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String principal = (String) authentication.getPrincipal();
		if (!principalRequestValue.equals(principal)) {
			
			LOGGER.warn(ContentErrorCode.WARN_APIKEY.getCode() + " " + ContentErrorCode.WARN_APIKEY.getMessage());
			
			throw new BadCredentialsException("The API key was not found or not the expected value.");
		}
		authentication.setAuthenticated(true);
		return authentication;
	}

}
