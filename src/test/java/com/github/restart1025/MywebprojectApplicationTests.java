package com.github.restart1025;

import com.github.restart1025.entity.Person;
import com.github.restart1025.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MywebprojectApplicationTests {

    @Autowired
    private PersonService personService;

	@Test
	public void contextLoads() {

        List<Person> list = personService.findAll();
        for(Person person : list)
        {
            System.out.println(person.getUsername());
        }

	}

}
