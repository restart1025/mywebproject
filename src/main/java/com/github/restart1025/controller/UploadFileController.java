package com.github.restart1025.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.github.restart1025.entity.Person;
import com.github.restart1025.entity.UploadFile;
import com.github.restart1025.service.PersonService;
import com.github.restart1025.service.UploadFileSerivce;
import com.github.restart1025.util.QiNiuYun;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/uploadData")
public class UploadFileController {

    @Resource
    private UploadFileSerivce uploadFileSerivce;
    @Resource
    private PersonService personService;
    /**
     * 多文件上传 主要是使用了MultipartHttpServletRequest和MultipartFile
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String batchUpload(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");
        MultipartFile file = null;
        File saveFile = null;
        BufferedOutputStream stream = null;
        //保存文件到临时目录
        String savePath = request.getSession().getServletContext().getRealPath("/")
                + "/uploadFiles/";
        UploadFile uploadFile = null;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("personId", "restart1025");
        Person person = personService.getPersonByPersonId(map);

        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    // FileUtils.copyFile(file, new File("path", "newName"));
                    saveFile = new File(savePath + file.getOriginalFilename());
                    file.transferTo(saveFile);

                    String yunFileName = QiNiuYun.upload(saveFile, QiNiuYun.FILE);

                    uploadFile = new UploadFile();
                    uploadFile.setFileName(QiNiuYun.getFileName(saveFile.getName()));
                    uploadFile.setFilePath(QiNiuYun.FILEDOWNLOAD + yunFileName);
                    uploadFile.setFileSn(yunFileName);
                    uploadFile.setFileType(QiNiuYun.FILE);
                    uploadFile.setUploadTime(LocalDateTime.now());
                    uploadFile.setUploader(person);
                    uploadFileSerivce.insert(uploadFile);

                    System.out.println(yunFileName);
                    System.out.println("upload successful");
                } catch (Exception e) {
                    stream = null;
                    System.out.println("You failed to upload " + i + " => " + e.getMessage());
                } finally {
//                    if(saveFile.exists())
//                    {
//                        saveFile.delete();
//                    }
                }
            } else {
                System.out.println("You failed to upload " + i + " because the file was empty.");
            }
        }
        return "success";
    }

    @RequestMapping(value = "/showData", method = RequestMethod.POST)
    public JSONObject showData(@RequestBody Map<String, Object> map) {
        return  uploadFileSerivce.queryUploadFilesByPersonId(map);
    }
}
