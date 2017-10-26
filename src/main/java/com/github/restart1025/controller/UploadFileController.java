package com.github.restart1025.controller;

import com.alibaba.fastjson.JSONObject;
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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/uploadData")
public class UploadFileController {

    @Resource
    private UploadFileSerivce uploadFileSerivce;

    /**
     * 多文件上传 主要是使用了MultipartHttpServletRequest和MultipartFile
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String, Object> batchUpload(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");

        //保存文件到临时目录
        String savePath = request.getSession().getServletContext().getRealPath("/")
                + "/uploadFiles/";

        Map<String, Object> map = uploadFileSerivce.batchUpload(files, "restart1025", savePath);

        return map;
    }

    @RequestMapping(value = "/showData", method = RequestMethod.POST)
    public JSONObject showData(@RequestBody Map<String, Object> map) {
        return  uploadFileSerivce.queryUploadFilesByPersonId(map);
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public void testDownload(HttpServletResponse res, String filePath,
                             String fileName) {

        System.out.println("filePath" + filePath);
        System.out.println("fileName" + fileName);

        fileName = fileName + this.getFileExtByFileName(filePath);

        System.out.println("fileName after" + fileName);
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
//            bis = new BufferedInputStream(new FileInputStream(new File("D:\\testLog\\"
//                    + fileName)));
            bis = new BufferedInputStream(QiNiuYun.download(filePath));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("success");
    }

    /**
     * 根据文件名获取文件后缀
     * @param fileName 文件名
     * @return
     */
    private String getFileExtByFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }
}
