package com.github.restart1025.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    /**
     * 转发JSP页面请求
     * @param forName
     * @return
     */
    @RequestMapping("/{forName}")
    public String jspForword(@PathVariable String forName) {
        return forName;
    }

    /**
     * 转发JSP页面请求
     * @param forName
     * @return
     */
    @RequestMapping("/{forName}/{view}")
    public String jspChildForword(@PathVariable String forName, @PathVariable String view) {
        return forName + "/" +view;
    }

}
