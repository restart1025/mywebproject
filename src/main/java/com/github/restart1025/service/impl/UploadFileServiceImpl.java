package com.github.restart1025.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.restart1025.dao.UploadFileDao;
import com.github.restart1025.entity.UploadFile;
import com.github.restart1025.service.UploadFileSerivce;
import com.github.restart1025.util.QiNiuYun;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("uploadFileService")
public class UploadFileServiceImpl implements UploadFileSerivce {


    private static Logger logger = LoggerFactory.getLogger(UploadFileServiceImpl.class);

    @Resource
    private UploadFileDao uploadFileDao;

    @Override
    public JSONObject queryUploadFilesByPersonId(Map<String, Object> map) {

        Integer pageNum = MapUtils.getInteger(map,"offset");
        Integer pageSize = MapUtils.getInteger(map,"limit");

        PageHelper.startPage(pageNum, pageSize);
        List<UploadFile> list = uploadFileDao.queryUploadFilesByPersonId(map);

        PageInfo<UploadFile> page = new PageInfo<UploadFile>(list);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", page.getTotal());

        List<UploadFile> result = page.getList();

        JSONArray jsonArray = new JSONArray();
        for( UploadFile uploadFile : result )
        {
            JSONObject jo = new JSONObject();
            jo.put("fileSn", uploadFile.getFileSn());
            jo.put("fileName", uploadFile.getFileName());
            jo.put("filePath", uploadFile.getFilePath());
            jo.put("fileType", uploadFile.getFileType());
            if( uploadFile.getUploader() != null )
            {
                jo.put("uploader", uploadFile.getUploader().getUsername());
            } else {
                jo.put("uploader", "无");
            }
            if( uploadFile.getUploadTime() != null )
            {
                jo.put("uploadTime", String.valueOf(uploadFile.getUploadTime()).replace("T", " "));
            } else {
                jo.put("uploadTime", "无");
            }
            jsonArray.add(jo);
        }
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @Override
    public List<UploadFile> findAll() {
        return uploadFileDao.findAll();
    }

    @Override
    public void insert(Map<String, Object> map) {
        uploadFileDao.insertUploadFile(map);
    }

    @Override
    public void deleteFile(Map<String, Object> map) {
        uploadFileDao.deleteFile(map);
    }

    @Override
    public Map<String, Object> batchUpload(List<MultipartFile> files, String personId, String savePath) {

        Map<String, Object> result = new HashMap<String, Object>();

        File saveFile = null;//上传文件流转化为File
        Map<String, Object> map = null;//文件保存参数

        for (MultipartFile file : files)
        {
            if (!file.isEmpty())
            {
                try {

                    //文件保存到本地
                    saveFile = new File(savePath + file.getOriginalFilename());
                    file.transferTo(saveFile);

                    //上传后返回文件编号
                    String yunFileName = QiNiuYun.upload(saveFile, QiNiuYun.FILE);

                    map = new HashMap<String, Object>();
                    map.put("fileName", QiNiuYun.getFileName(saveFile.getName()));
                    map.put("filePath", QiNiuYun.FILEDOWNLOAD + yunFileName);
                    map.put("fileSn", yunFileName);
                    map.put("fileType", QiNiuYun.FILE);
                    map.put("uploadTime", LocalDateTime.now());
                    map.put("uploader", personId);
                    this.insert(map);

                    logger.debug("upload successful fileName=" + yunFileName);
                    result.put("result", true);

                } catch (Exception e) {

                    logger.error("You failed to upload " + file.getName() + " because error.");
                    result.put("result", false);
                    result.put("errorCode", 1);
                    result.put("errorMsg", "上传出错");
                    break;

                } finally {

//                    if(saveFile.exists())
//                    {
//                        saveFile.delete();
//                    }

                }
            } else {
                logger.debug("You failed to upload " + file.getName() + " because the file was empty.");
                result.put("result", false);
                result.put("emptyCode", 1);
                result.put("emptyMsg", "上传文件为空");
                break;
            }
        }
        return result;
    }
}
