package fr.gouv.education.tribu.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.gouv.education.tribu.api.directory.model.Person;
import fr.gouv.education.tribu.api.directory.service.PersonService;
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
	private PersonService personService;
	
	@Autowired
	@Qualifier("appMap")
	private Map<String, ApiUser> appMap;	

	@Override
	public TribuApiResponse search(SearchForm search) throws RepositoryException, ContentServiceException, UserNotFoundException {
		
		
		ApiUser apiUser = appMap.get(search.getAppId());

		String userId = processUserId(search.getUser(), search.getHashnumen());

		
		NuxeoCommand command = context.getBean(SearchCommand.class, search, apiUser.getWorkspacePath());
		PaginableDocuments searchResults = (PaginableDocuments) repo.executeCommand(userId, command);
				
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


	private String processUserId(String userId, String hashnumen) throws UserNotFoundException {

		
		
		if(StringUtils.isNotBlank(hashnumen)) {
			// ldap search by hashnumen
			Person searchByNumen = personService.getEmptyPerson();
			searchByNumen.setHashNumen(hashnumen);
			List<Person> persons = personService.findByCriteria(searchByNumen);
			
			if(persons != null && persons.size() == 1) {
				Person person = persons.get(0);
				userId = person.getUid();
			}
			else {
				throw new UserNotFoundException("Person with numen "+hashnumen+ " not found");
			}
				
		}
		return userId;
	}

	
	private ContentDto toDto(Document document, String compomentName) throws ContentServiceException {
		ContentDto dto = (ContentDto) context.getBean(compomentName);
		return dto.toDto(document);
	}

	@Override
	public DownloadUrlResponse download(DownloadForm dlForm) throws RepositoryException, ContentServiceException, UserNotFoundException {
		
		String userId = processUserId(dlForm.getUser(), dlForm.getHashnumen());

		
		NuxeoCommand command = context.getBean(GetDocumentCommand.class, dlForm);
		PaginableDocuments searchResults = (PaginableDocuments) repo.executeCommand(userId, command);

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
	public BinaryContent startDownload(String uuid, String userId) throws RepositoryException, UserNotFoundException {
		
		
		NuxeoCommand command = context.getBean(FileContentCommand.class, uuid);	
		BinaryContent binaryContent = (BinaryContent) repo.executeCommand(userId, command);
		
		return binaryContent;
		
	}
}
