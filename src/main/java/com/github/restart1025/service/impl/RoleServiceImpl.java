package com.github.restart1025.service.impl;

import com.github.restart1025.dao.RoleDao;
import com.github.restart1025.entity.Role;
import com.github.restart1025.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Override
    public Role getByRoleSn(Map<String, String> map) {
        return roleDao.getByRoleSn(map);
    }

    @Override
    public List<Role> getRoleByPersonId(Map<String, String> map) {
        return roleDao.getRoleByPersonId(map);
    }
}
