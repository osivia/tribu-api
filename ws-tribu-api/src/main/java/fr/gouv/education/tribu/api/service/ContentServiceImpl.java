package fr.gouv.education.tribu.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.gouv.education.tribu.api.model.ApiUser;
import fr.gouv.education.tribu.api.model.BinaryContent;
import fr.gouv.education.tribu.api.model.ContentDto;
import fr.gouv.education.tribu.api.model.DownloadForm;
import fr.gouv.education.tribu.api.model.DownloadUrlResponse;
import fr.gouv.education.tribu.api.model.SearchContentDto;
import fr.gouv.education.tribu.api.model.SearchForm;
import fr.gouv.education.tribu.api.model.TribuApiResponse;
import fr.gouv.education.tribu.api.repo.NuxeoCommand;
import fr.gouv.education.tribu.api.repo.NuxeoRepo;
import fr.gouv.education.tribu.api.repo.RepositoryException;
import fr.gouv.education.tribu.api.repo.commands.FileContentCommand;
import fr.gouv.education.tribu.api.repo.commands.FileContentStreamCommand;
import fr.gouv.education.tribu.api.repo.commands.GetDocumentCommand;
import fr.gouv.education.tribu.api.repo.commands.SearchCommand;
import fr.gouv.education.tribu.api.service.token.DownloadToken;
import fr.gouv.education.tribu.api.service.token.DownloadTokenService;



@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private DownloadTokenService tokenService;
	
	@Autowired
	private NuxeoRepo repo;
	
	@Autowired
	@Qualifier("appMap")
	private Map<String, ApiUser> appMap;	

	@Override
	public TribuApiResponse search(SearchForm search) throws RepositoryException, ContentServiceException {
		
		ApiUser apiUser = appMap.get(search.getAppId());
		
		NuxeoCommand command = context.getBean(SearchCommand.class, search, apiUser.getWorkspacePath());
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
			
			// == put in cache ==
			DownloadToken tokenObject = context.getBean(DownloadToken.class);
			tokenObject.setDocUuid(searchResults.get(0).getId());
			tokenObject.setAppId(dlForm.getAppId());
			tokenObject.setUser(dlForm.getUser());
			
			tokenService.putInCache(tokenObject);

			response.toResponse(tokenObject.getToken());
		}
		
		return response;
	}

	@Override
    public DownloadToken checkToken(String docUuid, String token) {
    
    	DownloadToken tokenInCache = tokenService.getInCache(token);
    	
    	if(tokenInCache != null) {
    		
    		if(tokenInCache.getDocUuid().equals(docUuid)) {
    			
    			tokenService.removeFromCache(token);
    			
    			return tokenInCache;
    			
    		}
    	}
    	
    	return null; // in case of errors
    	
    }


	@Override
	public BinaryContent startDownload(String uuid, String appId) throws RepositoryException {
		
		
		NuxeoCommand command = context.getBean(FileContentCommand.class, uuid);
		//NuxeoCommand command = context.getBean(FileContentStreamCommand.class, uuid);
		
		BinaryContent binaryContent = (BinaryContent) repo.executeCommand(appId, command);
		
		return binaryContent;
		
	}
}
