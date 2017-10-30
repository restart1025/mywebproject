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
        JSONObject jo = new JSONObject();
        jo.put("result", false);
        try {
            if(MapUtils.getString(map, "fileSn") != null)
            {
                String personId = (String) SecurityUtils.getSubject().getPrincipal();
                if(personId != null)
                {
                    if(personId.equals(MapUtils.getString(map, "uploader")))
                    {
                        map.put("personId", personId);
                        uploadFileSerivce.deleteFile(map);
                        jo.put("result", true);
                    } else {
                        jo.put("msg", "只能删除自己上传的文件!");
                    }
                } else {
                    jo.put("msg", "登录超时, 请重新登录!");
                }
            }
        } catch (Exception e) {
            jo.put("msg", "程序异常, 请刷新页面后重试!");
        }
        return jo;
    }
    /**
     * 文件下载
     * @param res
     * @param filePath
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public void testDownload(HttpServletResponse res, String filePath,
                             String fileName) throws UnsupportedEncodingException {

        logger.info("filePath : " + filePath);
        logger.info("fileName : " + fileName);

        fileName = URLEncoder.encode(fileName + this.getFileExtByFileName(filePath),"UTF-8");

        logger.info("fileName after : " + fileName);
        //设置响应头和客户端保存文件名
        res.setCharacterEncoding("utf-8");
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        InputStream bis = null;
        OutputStream os;
        try {
            os = res.getOutputStream();
//            bis = new BufferedInputStream(new FileInputStream(new File("D:\\testLog\\"
//                    + fileName)));
            bis = QiNiuYun.download(filePath);
            if( bis != null )
            {
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
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
