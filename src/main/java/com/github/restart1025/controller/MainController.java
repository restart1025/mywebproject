package com.github.restart1025.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {


    /**
     * 转发JSP页面请求
     * @return
     */
    @RequestMapping("/")
    public String main() {
        return "main";
    }
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
     * 转发展示文件页面请求
     * @return
     */
    @RequestMapping("/showFile/showFile")
    public String showFile() {
        return "showFile/showFile";
    }
    /**
     * 转发上传文件页面请求
     * @return
     */
    @RequestMapping("/uploadFile/uploadFile")
    public String uploadFile() {
        return "uploadFile/uploadFile";
    }
    /**
     * 转发主页页面请求
     * @return
     */
    @RequestMapping("/index/pin_board")
    public String index() {
        return "index/pin_board";
    }


}
