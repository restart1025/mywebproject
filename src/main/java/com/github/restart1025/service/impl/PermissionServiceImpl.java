package com.github.restart1025.service.impl;

import com.github.restart1025.dao.PermissionDao;
import com.github.restart1025.entity.Permission;
import com.github.restart1025.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionDao permissionDao;

    @Override
    public Permission getByPermissionSn(Map<String, String> map) {
        return permissionDao.getByPermissionSn(map);
    }

    @Override
    public List<Permission> getPermissionByRoleSn(Map<String, String> map) {
        return permissionDao.getPermissionByRoleSn(map);
    }
}
