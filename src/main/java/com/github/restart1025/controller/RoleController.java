package com.github.restart1025.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.restart1025.service.RoleService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Resource
    private RoleService roleService;

    @RequestMapping(value = "/showData", method = RequestMethod.POST)
    public JSONObject showData(@RequestBody Map<String, Object> map) {
        String personId = (String) SecurityUtils.getSubject().getPrincipal();

        logger.debug(" role showData personId is : " + personId);
        map.put("personId", personId);

        return roleService.getRoleByPersonId(map);
    }

}
