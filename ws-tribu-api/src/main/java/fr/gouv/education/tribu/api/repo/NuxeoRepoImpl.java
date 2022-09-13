package fr.gouv.education.tribu.api.repo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private final Log log = LogFactory.getLog(NuxeoRepoImpl.class.getName());
	
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
			if(log.isDebugEnabled()) {
				log.debug("About to get a connexion from the pool. Idle : "+pool.getNumIdle()+ ", active : "+pool.getNumActive()+", waiters : "+pool.getNumWaiters());
			}

			client = pool.borrowObject();

			if(log.isDebugEnabled()) {
				log.debug("Got connexion from the pool "+client.hashCode()+". Idle : "+pool.getNumIdle()+ ", active : "+pool.getNumActive()+", waiters : "+pool.getNumWaiters());
			}
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

			if(log.isDebugEnabled()) {
				log.debug("Release connexion to the pool. Idle : "+pool.getNumIdle()+ ", active : "+pool.getNumActive()+", waiters : "+pool.getNumWaiters());
			}


		}
		
	}



}
