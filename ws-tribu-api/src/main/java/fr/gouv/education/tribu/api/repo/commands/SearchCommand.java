package fr.gouv.education.tribu.api.repo.commands;

import java.util.Date;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
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

	public SearchCommand(SearchForm search) {
		this.search = search;
		
	}
	
	@Override
	protected StringBuilder getQuery() {
        // Query
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document WHERE ");
        query.append(" ecm:mixinType <> 'HiddenInNavigation' AND ecm:currentLifeCycleState <> 'deleted'  AND ecm:isCheckedInVersion = 0");
        query.append(" AND ecm:mixinType <> 'isLocalPublishLive'");
        
        // TODO adaptation 4.4
        if(StringUtils.isNotBlank(search.getFulltext())) {
        	query.append(" /*+ES: ");

        	query.append("FULLTEXT('").append(search.getFulltext()).append("') */");
        }
        
        if(StringUtils.isNotBlank(search.getTitle())) {
        	query.append(" AND dc:title ILIKE '%"+search.getTitle()+"%' ");
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
