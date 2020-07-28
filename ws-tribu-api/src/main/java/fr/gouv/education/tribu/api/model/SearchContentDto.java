/**
 * 
 */
package fr.gouv.education.tribu.api.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.gouv.education.tribu.api.service.ContentServiceException;
import io.swagger.annotations.ApiModel;


/**
 * @author Loïc Billon
 *
 */
@Component(value = SearchContentDto.COMPONENT_NAME)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchContentDto extends ContentDto {
	
	public static final String COMPONENT_NAME = "SearchContentDto";
	
//	/** Highlighted fields from Es fulltext search. */
//	private Map<String, List<Object>> highlight;
//
//	public Map<String, List<Object>> getHighlight() {
//		return highlight;
//	}
//
//	public void setHighlight(Map<String, List<Object>> highlight) {
//		this.highlight = highlight;
//	}
	
	@Override
	public ContentDto toDto(Document document) throws ContentServiceException {
		
		super.toDto(document);
		
//		PropertyMap highlight = document.getHighlight();
//		
//		if(!highlight.isEmpty()) {
//			Map<String, List<Object>> map = new HashMap<String, List<Object>>();
//			for(Entry<String, Object> entry : highlight.map().entrySet()) {
//				map.put(entry.getKey(), ((PropertyList) entry.getValue()).list());
//			}
//			
//			this.setHighlight(map);
//		}
		
		return this;
	}
}
