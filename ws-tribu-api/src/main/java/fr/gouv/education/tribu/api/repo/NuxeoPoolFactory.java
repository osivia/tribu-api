package fr.gouv.education.tribu.api.repo;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import fr.gouv.education.tribu.api.repo.commands.TestNxCommand;

/**
 * Implements the pool to nuxeo behavior
 * 
 * @author Loïc Billon
 *
 */
@Repository
public class NuxeoPoolFactory implements PooledObjectFactory<HttpAutomationClient> {

	/** Nuxeo context. */
	public static final String NUXEO_CONTEXT = "/nuxeo";

	@Value("${nuxeo.privateHost}")
	private String host;
	
	@Value("${nuxeo.privatePort}")
	private String port;
		
	@Value("${nuxeo.timeout:30000}")
	private Integer timeout;
	
	public PooledObject<HttpAutomationClient> makeObject() throws Exception {
      String url = getPrivateBaseUri().toString() + "/site/automation";
      HttpAutomationClient client = new HttpAutomationClient(url, timeout);
      DefaultPooledObject<HttpAutomationClient> pooled = new  DefaultPooledObject<HttpAutomationClient>(client);
      
      return pooled;
	}

	public void destroyObject(PooledObject<HttpAutomationClient> p) throws Exception {
		HttpAutomationClient client = p.getObject();
		client.shutdown();
		
	}

	public boolean validateObject(PooledObject<HttpAutomationClient> p) {
		HttpAutomationClient client = p.getObject();
		Session session = client.getSession();
		
		TestNxCommand test = new TestNxCommand();
		try {
			test.execute(session);
		} catch (RepositoryException e) {

			return false;
		}
		
				
		return true;
	}

	public void activateObject(PooledObject<HttpAutomationClient> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void passivateObject(PooledObject<HttpAutomationClient> p) throws Exception {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Getter for Nuxeo private base URI (with context).
	 * 
	 * @return Nuxeo private base URI
	 * @throws URISyntaxException 
	 */
	public final URI getPrivateBaseUri() throws URISyntaxException {

		return new URI("http://" + host + ":" + port + NUXEO_CONTEXT);
	} 


}
