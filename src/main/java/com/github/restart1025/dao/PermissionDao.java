package com.github.restart1025.dao;

import com.github.restart1025.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionDao {
    /**
     * 根据资源编号查找资源
     * @param map key:permissionSn
     * @return
     */
    Permission getByPermissionSn(Map<String, String> map);

    /**
     * 根据角色编号查找所拥有的资源
     * @param map key:roleSn
     * @return
     */
    List<Permission> getPermissionByRoleSn(Map<String, String> map);
}
