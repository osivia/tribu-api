package fr.gouv.education.tribu.api.service.token;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

/**
 * Logger for cache
 */
public class CacheEventLogger implements CacheEventListener<String, DownloadToken> {

	protected static final Log LOGGER = LogFactory.getLog("tribu-tokens");
	private static final Object BLANK = " ";
	
	@Override
	public void onEvent(CacheEvent<? extends String, ? extends DownloadToken> event) {

		StringBuilder sb = new StringBuilder();
		sb.append(event.getType());
		sb.append(BLANK);
		sb.append(event.getKey());
		sb.append(BLANK);
		sb.append(event.getNewValue());

		
		LOGGER.info(sb.toString());
		
	}

	
}
