package com.github.restart1025.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.restart1025.service.UploadFileSerivce;
import com.github.restart1025.util.QiNiuYun;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/uploadData")
public class UploadFileController {

    private static Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @Resource
    private UploadFileSerivce uploadFileSerivce;

    /**
     * 多文件上传 主要是使用了MultipartHttpServletRequest和MultipartFile
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    //@RequiresPermissions("user:query")//权限管理;
    public Map<String, Object> batchUpload(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");

        //保存文件到临时目录
        String savePath = request.getSession().getServletContext().getRealPath("/")
                + "/uploadFiles/";

        Map<String, Object> map = uploadFileSerivce.batchUpload(files, "restart1025", savePath);

        return map;
    }

    /**
     * 文件展示
     * @param map
     * @return
     */
    @RequestMapping(value = "/showData", method = RequestMethod.POST)
    public JSONObject showData(@RequestBody Map<String, Object> map) {
        String personId = (String) SecurityUtils.getSubject().getPrincipal();

        // 暂时设置personId为空时, 给定一个默认值, 以后会取消
        logger.debug(" login personId is : " + personId);
        if(StringUtils.isEmpty(personId))
            personId = "restart1025";

        map.put("personId", personId);
        return  uploadFileSerivce.queryUploadFilesByPersonId(map);
    }

    /**
     * 删除文件
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public JSONObject deleteFile(@RequestBody Map<String, Object> map) {
        return uploadFileSerivce.deleteFile(map);
    }
    /**
     * 文件下载
     * @param res
     * @param filePath
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public void fileDownload(HttpServletResponse res, String filePath,
                             String fileName) throws UnsupportedEncodingException {

        logger.info("filePath : " + filePath);
        logger.info("fileName : " + fileName);

        uploadFileSerivce.fileDownload(res, filePath, fileName);
    }

}
