package com.github.restart1025.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.restart1025.dao.UploadFileDao;
import com.github.restart1025.entity.UploadFile;
import com.github.restart1025.service.UploadFileSerivce;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("uploadFileService")
public class UploadFileServiceImpl implements UploadFileSerivce {

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
    public void insert(UploadFile uploadFile) {
        uploadFileDao.insertUploadFile(uploadFile);
    }
}
