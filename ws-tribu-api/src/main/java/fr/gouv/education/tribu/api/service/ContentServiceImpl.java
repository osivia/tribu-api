package fr.gouv.education.tribu.api.service;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.gouv.education.tribu.api.model.ContentDto;
import fr.gouv.education.tribu.api.model.DownloadForm;
import fr.gouv.education.tribu.api.model.DownloadUrlResponse;
import fr.gouv.education.tribu.api.model.SearchContentDto;
import fr.gouv.education.tribu.api.model.SearchForm;
import fr.gouv.education.tribu.api.model.TribuApiResponse;
import fr.gouv.education.tribu.api.repo.NuxeoCommand;
import fr.gouv.education.tribu.api.repo.NuxeoRepo;
import fr.gouv.education.tribu.api.repo.RepositoryException;
import fr.gouv.education.tribu.api.repo.commands.GetDocumentCommand;
import fr.gouv.education.tribu.api.repo.commands.SearchCommand;



@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private ApplicationContext context;
	

	@Autowired
	private NuxeoRepo repo;

	@Override
	public TribuApiResponse search(SearchForm search) throws RepositoryException, ContentServiceException {
		
		NuxeoCommand command = context.getBean(SearchCommand.class, search);
		PaginableDocuments searchResults = (PaginableDocuments) repo.executeCommand(search.getAppId(), command);
		
		
		List<ContentDto> docs = new ArrayList<ContentDto>();
		for(Document result : searchResults) {
			SearchContentDto dto = (SearchContentDto) toDto(result, SearchContentDto.COMPONENT_NAME);
			docs.add(dto);
		}
		
		TribuApiResponse response = new TribuApiResponse(docs);
		response.setCurrentPageIndex(searchResults.getCurrentPageIndex());
		response.setNumberOfPages(searchResults.getNumberOfPages());
		response.setResultsCount(searchResults.getResultsCount());
		response.setPageSize(searchResults.getPageSize());
		
		return response;
	}

	/**
	 * Convert a nuxeo document to webservices dto 
	 * 
	 * @param document
	 * @return
	 * @throws ContentServiceException
	 */
	private ContentDto toDto(Document document) throws ContentServiceException {
		return toDto(document, ContentDto.COMPONENT_NAME);
	}
	
	private ContentDto toDto(Document document, String compomentName) throws ContentServiceException {
		ContentDto dto = (ContentDto) context.getBean(compomentName);
		return dto.toDto(document);
	}

	@Override
	public DownloadUrlResponse download(DownloadForm dlForm) throws RepositoryException, ContentServiceException {
		
		NuxeoCommand command = context.getBean(GetDocumentCommand.class, dlForm);
		PaginableDocuments searchResults = (PaginableDocuments) repo.executeCommand(dlForm.getAppId(), command);

		List<ContentDto> docs = new ArrayList<ContentDto>(1);
		for(Document result : searchResults) {
			SearchContentDto dto = (SearchContentDto) toDto(result, SearchContentDto.COMPONENT_NAME);
			docs.add(dto);
		}
		
		DownloadUrlResponse response = context.getBean(DownloadUrlResponse.class, docs);
		
		if(searchResults.size() > 0) {

			response.toResponse();
		}
		
		return response;
	}
}
