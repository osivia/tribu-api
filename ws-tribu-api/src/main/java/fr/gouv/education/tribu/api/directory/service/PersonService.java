package fr.gouv.education.tribu.api.directory.service;

import java.util.List;

import fr.gouv.education.tribu.api.directory.model.Person;

/**
 * Service to request person
 * @author Loïc Billon
 * @since 4.4
 */
public interface PersonService {


	/**
	 * Get a person with no valued fields (for search)
	 * @return empty object person
	 */
	public Person getEmptyPerson();

	/**
	 * Get a person by criteria represented by a person vith filled fields
	 * @param p a person 
	 * @return a list of person
	 */
	List<Person> findByCriteria(Person p);

	
	
}
