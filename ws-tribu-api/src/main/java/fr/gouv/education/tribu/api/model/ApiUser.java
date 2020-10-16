package fr.gouv.education.tribu.api.model;

import java.io.Serializable;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Configuration class used to filter nuxeo user and his workspace scope
 * with given appId
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ApiUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 903745658142001247L;

	private String nuxeoUser;
	
	private String workspacePath;


	public String getNuxeoUser() {
		return nuxeoUser;
	}

	public String getWorkspacePath() {
		return workspacePath;
	}

	public void setNuxeoUser(String nuxeoUser) {
		this.nuxeoUser = nuxeoUser;
	}

	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath;
	}

	
}
