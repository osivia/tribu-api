package fr.gouv.education.tribu.api.repo.commands;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tomcat.util.digester.SetPropertiesRule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.gouv.education.tribu.api.model.SearchForm;
import fr.gouv.education.tribu.api.repo.NuxeoQueryCommand;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchCommand  extends NuxeoQueryCommand {

    /** Backend date pattern. */
    private static final String BACKEND_DATE_PATTERN = "yyyy-MM-dd";
	
	private SearchForm search;

	private String workspacePath;

	public SearchCommand(SearchForm search, String workspacePath) {
		
		super(search);
		
		this.search = search;
		this.workspacePath = workspacePath;
				
	}
	
	@Override
	protected StringBuilder getQuery() {
        // Query
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document WHERE ");
        query.append(" ecm:mixinType <> 'HiddenInNavigation' AND ecm:currentLifeCycleState <> 'deleted'  AND ecm:isCheckedInVersion = 0");
        query.append(" AND ecm:mixinType = 'Downloadable' ");
        query.append(" AND ecm:mixinType <> 'isLocalPublishLive' ");
        
        if(StringUtils.isNotBlank(search.getFulltext())) {
        	
            query.append(" AND ");
            
        	// Remove accents in search query
        	String searchStr = Normalizer.normalize(search.getFulltext(), Normalizer.Form.NFD);
        	searchStr = searchStr.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        	// Remove special chars
        	searchStr = searchStr.replaceAll("[^A-Za-z0-9 ]", " ");
        	
            String[] keyWds = StringUtils.split(searchStr);
            Iterator<String> itKeyWords = Arrays.asList(keyWds).iterator();

            while (itKeyWords.hasNext()) {
                //String keyWord = StringUtils.replace(itKeyWords.next(), "'", "\\'");
            	String keyWord = itKeyWords.next();
            	
                query.append("(ecm:fulltext = '");
                query.append(keyWord);
                query.append("' OR dc:title ILIKE '%");
                query.append(keyWord);
                query.append("%')");

                if (itKeyWords.hasNext()) {
                	query.append(" AND ");
                }
            }
        }
        
        if(StringUtils.isNotBlank(search.getTitle())) {
        	
            query.append(" AND ");
            
        	// Remove accents in search query
        	String titleStr = Normalizer.normalize(search.getTitle(), Normalizer.Form.NFD);
        	titleStr = titleStr.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        	// Remove special chars
        	titleStr = titleStr.replaceAll("[^A-Za-z0-9 ]", " ");
        	
            String[] keyWds = StringUtils.split(titleStr);
            Iterator<String> itKeyWords = Arrays.asList(keyWds).iterator();

            while (itKeyWords.hasNext()) {
                //String keyWord = StringUtils.replace(itKeyWords.next(), "'", "\\'");
            	String keyWord = itKeyWords.next();
            	
                query.append("dc:title ILIKE '%");
                query.append(keyWord);
                query.append("%'");

                if (itKeyWords.hasNext()) {
                	query.append(" AND ");
                }
            }
        	
        	//query.append(" AND dc:title ILIKE '%"+search.getTitle()+"%' ");
        }
        
        if(StringUtils.isNotBlank(workspacePath)) {
        	query.append(" AND ecm:path STARTSWITH '"+workspacePath+"' ");
        }
        
        if(search.getModificationBeginDate() != null && search.getModificationEndDate() != null) {

        	// Backend dates format
            String from = DateFormatUtils.format(search.getModificationBeginDate(), BACKEND_DATE_PATTERN);
        	// Add one day to end date
            Date endDate = DateUtils.addDays(search.getModificationEndDate(), 1);
            String to = DateFormatUtils.format(endDate, BACKEND_DATE_PATTERN);
            
            query.append(" AND (dc:modified BETWEEN DATE '");
            query.append(from);
            query.append("' AND DATE '");
            query.append(to);
            query.append("') ");
        }
        
        if ((this.search.getSort() != null) && StringUtils.isNotEmpty(this.search.getSort().getOrderBy())) {
            query.append("ORDER BY ");
            query.append(this.search.getSort().getOrderBy());

            if (BooleanUtils.isTrue(this.search.getReversedSort())) {
                query.append(" DESC");
            } else {
                query.append(" ASC");
            }
        }
       
        return query;
	}
	
	@Override
	protected String getDocumentProperties() {
		return "*";
	}

}
