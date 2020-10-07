package fr.gouv.education.tribu.api.service.token;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Token for download a file, will be cached
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DownloadToken implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4359613243942793114L;

	private final String token;
	
	private String user;
	
	private String appId;
	
	private String docUuid;
	
	public DownloadToken() {
		UUID uuid = UUID.randomUUID();
		this.token = uuid.toString();
	}

	public String getToken() {
		return token;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getDocUuid() {
		return docUuid;
	}

	public void setDocUuid(String docUuid) {
		this.docUuid = docUuid;
	}
	
	@Override
	public String toString() {
		return "docuuid:"+docUuid+ " for "+user+"@"+appId;
	}
}
