package fr.gouv.education.tribu.api.repo;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.gouv.education.tribu.api.model.TribuApiQueryForm;


/**
 * Generic properties of nuxeo command for ES queries
 * 
 * @author Loïc Billon
 *
 */
public abstract class NuxeoQueryCommand implements NuxeoCommand {
	
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

	
	        objects = request.execute();
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
	
}
