package fr.gouv.education.tribu.api.repo.commands;

import fr.gouv.education.tribu.api.repo.NuxeoQueryCommand;

public class TestNxCommand extends NuxeoQueryCommand {


	@Override
	protected StringBuilder getQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document ");
        return query;
	}
	
	protected Integer getPageSize() {

		return 1;
	}

}
