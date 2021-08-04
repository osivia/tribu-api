package fr.gouv.education.tribu.api.directory.dao;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import fr.gouv.education.tribu.api.DirectoryFrameworkConfiguration;
import fr.gouv.education.tribu.api.directory.model.Person;


/**
 * @author Loïc Billon
 *
 */
@Repository("personDao")
public class PersonDaoImpl implements PersonDao {


	@Value(DirectoryFrameworkConfiguration.BASE_DN_CONF)
	private String base;


	@Value("${ldap.searchperson.maxTime:5000}") 
	private int searchTimeLimit;
	
	@Value("${ldap.searchperson.maxResults:750}") 
	private int searchMaxResults;
	
	@Autowired
	protected Person sample;
	
	
	@Autowired
	@Qualifier("ldapTemplate")
	protected LdapTemplate template;
//	
	
	private SearchControls controls;
		
	

    /**
     * {@inheritDoc}
     */
	@Override	
    public Name buildBaseDn() {
        return LdapNameBuilder.newInstance(base).add("ou=users").build();
    }

    /**
     * {@inheritDoc}
     */
	@Override	
    public Name buildDn(String uid)  {
    	return LdapNameBuilder.newInstance(buildBaseDn()).add("uid=" + uid).build();
    }
    
    

	@Override
	public List<Person> findByCriteria(Person p) {

		OrFilter filter = MappingHelper.generateOrFilter(p);
				
		return (List<Person>) template.find(buildBaseDn(), filter, getSearchControls() , sample.getClass());
		
	}
	
	
	
	/**
	 * Query optimization
	 * @return
	 */
	protected SearchControls getSearchControls() {
		
		if(controls == null) {
			controls = new SearchControls();
			
			controls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
			
			controls.setTimeLimit(searchTimeLimit);
			
			controls.setCountLimit(searchMaxResults);
			
		}
		
		return controls;
	}

	
}
