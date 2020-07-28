package fr.gouv.education.tribu.api.repo.commands;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.gouv.education.tribu.api.model.DownloadForm;
import fr.gouv.education.tribu.api.repo.NuxeoQueryCommand;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetDocumentCommand extends NuxeoQueryCommand {

	private DownloadForm dlForm;

	public GetDocumentCommand(DownloadForm dlForm) {
		this.dlForm = dlForm;
	}
	
	
	@Override
	protected StringBuilder getQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document WHERE ecm:uuid = '"+dlForm.getUuid()+ "'");
        return query;
	}
	
	protected Integer getPageSize() {

		return 1;
	}
	
}
