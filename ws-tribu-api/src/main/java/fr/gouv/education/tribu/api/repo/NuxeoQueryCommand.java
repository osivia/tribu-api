package fr.gouv.education.tribu.api.repo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Documents;

import fr.gouv.education.tribu.api.model.TribuApiQueryForm;


/**
 * Generic properties of nuxeo command for ES queries
 * 
 * @author Loïc Billon
 *
 */
public abstract class NuxeoQueryCommand implements NuxeoCommand {
	
	protected static final Log LOGGER = LogFactory.getLog("tribu-queries");
	protected static final String LOG_SEPARATOR = " ";
	
	private static final String DEFAULT_SCHEMAS = "dublincore,toutatice";
	
	private static final Integer DEFAULT_PAGESIZE = 25;
	
	private final Integer pageSize;
	
	private final Integer pageNumber;
	
	public NuxeoQueryCommand() {
		pageSize = DEFAULT_PAGESIZE;
		pageNumber = 0;
	}
	
	public NuxeoQueryCommand(TribuApiQueryForm form) {
		if(form.getPageSize() != null) {
			pageSize = form.getPageSize();
		}
		else pageSize = DEFAULT_PAGESIZE;
		
		if(form.getPageNumber() != null) {
			pageNumber = form.getPageNumber();
		}
		else pageNumber = 0;
		
	}
	
	public Object execute(Session nuxeoSession) throws RepositoryException {
        
		
        Object objects;
        try {
	        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
	        request.setHeader("X-NXDocumentProperties", getDocumentProperties());
	        request.set("query", getQuery().toString());
	        request.set("pageSize", getPageSize());
	        request.set("currentPageIndex", getPageNumber());

	        
	        long startTime = System.currentTimeMillis();
	
	        objects = request.execute();
	        
	        if(LOGGER.isDebugEnabled()) {
	        	
	        	LOGGER.debug(logMsg(request, objects, startTime));
	        	
	        	
	        }
        }
        catch(Exception e) {
        	throw new RepositoryException("original request from "+this.getClass().getSimpleName()+" : "+getQuery().toString(), e);
        }
        
        return objects;
	}





	protected abstract StringBuilder getQuery();

	
	protected String getDocumentProperties() {
		return DEFAULT_SCHEMAS;
	}
	protected Integer getPageSize() {
		return pageSize;
	}
	
	protected Integer getPageNumber() {
		return pageNumber;
	}
	
	public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
//        builder.append("/");
//        builder.append(this.userName);
        return builder.toString();
	}
	
	
	
	private String logMsg(OperationRequest request, Object objects, long startTime) {
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		
		StringBuilder sb = new StringBuilder();
		sb.append(getId());
		sb.append(LOG_SEPARATOR);
		sb.append("user=");
		sb.append(request.getSession().getLogin().getUsername());
//		sb.append(request.getOperation());
		sb.append(LOG_SEPARATOR);
		sb.append(request.getHeaders());
		sb.append(LOG_SEPARATOR);
		sb.append(request.getParameters());
		sb.append(LOG_SEPARATOR);
		sb.append(objects);

		if(objects instanceof Documents) {
			sb.append(LOG_SEPARATOR);

			Documents docs = (Documents) objects;
			sb.append("resultCount=");
			sb.append(docs.size());
		}
		sb.append(LOG_SEPARATOR);
		sb.append("elasped=");
		sb.append(elapsedTime);
		
		return sb.toString();
	}
	
}
