package fr.gouv.education.tribu.api.repo;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.jaxrs.spi.auth.PortalSSOAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.gouv.education.tribu.api.service.UserNotFoundException;

@Service
public class NuxeoRepoImpl implements NuxeoRepo {

	
	@Value("${nuxeo.secretKey}")
	private String secretKey;
	
//	@Autowired
//	@Qualifier("appMap")
//	private Map<String, ApiUser> appMap;

	
	@Autowired
	private GenericObjectPool<HttpAutomationClient> pool;

	public Object executeCommand(String userId, NuxeoCommand command) throws RepositoryException, UserNotFoundException {
		
		HttpAutomationClient client = null;
		
		
		if(userId == null) {
			throw new RepositoryException("Unable to get a valid user "+userId);
		}
		
		try {
			client = pool.borrowObject();
		} catch (Exception e) {
			throw new RepositoryException("Unable to get a connection to nuxeo from the pool",e);
		}
		
		
		try {
			
	        if (userId != null) {
	            client.setRequestInterceptor(new PortalSSOAuthInterceptor(secretKey, userId));
	        }
	        
	        Session session;
			try {
				session = client.getSession();
			} catch (Exception e) {
				throw new UserNotFoundException("Unable to login with "+userId, e);

			}
	
			Object object = command.execute(session);

			return object;
		
		}
		finally {
			pool.returnObject(client);

		}
		
	}



}
