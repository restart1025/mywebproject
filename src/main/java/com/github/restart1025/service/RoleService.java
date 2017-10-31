package com.github.restart1025.service;

import com.alibaba.fastjson.JSONObject;
import com.github.restart1025.entity.Role;

import java.util.Map;

public interface RoleService {
    /**
     * 根据角色编号查找角色
     * @param map key:roleSn
     * @return
     */
    Role getByRoleSn(Map<String, String> map);

    /**
     * 根据用户名查找所拥有的权限
     * @param map key:personId
     * @return
     */
    JSONObject getRoleByPersonId(Map<String, Object> map);
}
