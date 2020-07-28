package fr.gouv.education.tribu.api.repo;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;

/**
 * COnfigure a default pool for nuxeo connections
 * 
 * @author Loïc Billon
 *
 */
public class NuxeoPoolConfig extends GenericObjectPoolConfig<HttpAutomationClient> {
	
	
	/**
	 * Disable JMX to fix conflicts with default JMX commons-pool
	 */
	@Override
	public boolean getJmxEnabled() {

		return false;
	}
	
	@Override
	public long getMinEvictableIdleTimeMillis() {
		return 1000L * 60L * 10L;
	}
	
	@Override
	public boolean getTestWhileIdle() {
		return true;
	}
	
	@Override
	public long getTimeBetweenEvictionRunsMillis() {
		return 1000L * 60L * 5L;

	}

	
}
