package fr.gouv.education.tribu.api.directory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.gouv.education.tribu.api.directory.dao.PersonDao;
import fr.gouv.education.tribu.api.directory.model.Person;

/**
 * Impl of the person service
 *
 * @author Loïc Billon
 * @since 4.4
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {


    @Autowired
    protected ApplicationContext context;

    @Autowired
    protected Person sample;

    @Autowired
    protected PersonDao dao;


    @Override
    public Person getEmptyPerson() {
        return context.getBean(sample.getClass());
    }


    /* (non-Javadoc)
     * @see org.osivia.portal.api.directory.v2.service.PersonService#getPerson(java.lang.String)
     */
    @Override
    public List<Person> findByCriteria(Person search) {

        List<Person> persons = dao.findByCriteria(search);

        return persons;
    }



}
