package com.github.restart1025.entity;

import java.util.List;

public class Role extends BaseEntity {

    private String roleSn;
    private String roleName;
    private List<Person> persons;
    private List<Permission> permissions;

    public String getRoleSn() {
        return roleSn;
    }

    public void setRoleSn(String roleSn) {
        this.roleSn = roleSn;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
