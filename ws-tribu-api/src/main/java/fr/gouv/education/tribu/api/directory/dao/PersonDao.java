package fr.gouv.education.tribu.api.directory.dao;

import java.util.List;

import javax.naming.Name;

import fr.gouv.education.tribu.api.directory.model.Person;


/**
 * @author Loïc Billon
 *
 */
public interface PersonDao {


	/**
	 * @param ps
	 * @return
	 */
	List<Person> findByCriteria(Person p);


	/**
	 * Return the base DN of a person
	 * @return
	 */
	Name buildBaseDn();

	/**
	 * Return the full DN of a person
	 * @return
	 */
	Name buildDn(String uid);

}
