package com.github.restart1025.service.impl;

import com.github.restart1025.dao.PersonDao;
import com.github.restart1025.entity.Person;
import com.github.restart1025.service.PersonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("personService")
public class PersonServiceImpl implements PersonService {

    @Resource
    private PersonDao personDao;

    @Override
    public Person getPersonByPersonId(Map<String, Object> map) {
        return personDao.getPersonByPersonId(map);
    }

    @Override
    public List<Person> findAll() {
        return personDao.findAll();
    }

    @Override
    public List<Person> getPersonByRoleSn(Map<String, Object> map) {
        return personDao.getPersonByRoleSn(map);
    }

    @Override
    public void personAdd(Person person) {
        personDao.personAdd(person);
    }
}
