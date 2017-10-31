package com.github.restart1025.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.restart1025.dao.RoleDao;
import com.github.restart1025.entity.Role;
import com.github.restart1025.service.RoleService;
import org.apache.commons.collections.MapUtils;
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
    public JSONObject getRoleByPersonId(Map<String, Object> map) {

        PageHelper.startPage(MapUtils.getInteger(map,"offset"), MapUtils.getInteger(map,"limit"));
        List<Role> list = roleDao.getRoleByPersonId(map);

        PageInfo<Role> page = new PageInfo<Role>(list);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", page.getTotal());

        list = page.getList();
        JSONArray ja = new JSONArray();
        for(Role role : list)
        {
            JSONObject jo = new JSONObject();
            jo.put("id", role.getId());
            jo.put("roleSn", role.getRoleSn());
            jo.put("roleName", role.getRoleName());
            ja.add(jo);
        }
        jsonObject.put("rows", ja);
        return jsonObject;
    }
}
