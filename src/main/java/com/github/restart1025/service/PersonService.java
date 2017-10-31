package com.github.restart1025.service;

import com.github.restart1025.entity.Person;

import java.util.List;
import java.util.Map;

public interface PersonService {
    /**
     * 根据用户名查找人员信息
     * @param map key:personId
     * @return
     */
    Person getPersonByPersonId(Map<String, Object> map);

    /**
     * 查找所有的用户
     * @return
     */
    List<Person> findAll();

    /**
     * 查找拥有该角色的人
     * @param map key:roleSn
     * @return
     */
    List<Person> getPersonByRoleSn(Map<String, Object> map);

    /**
     * 新增人员信息
     * @param person
     */
    void personAdd(Person person);
}
