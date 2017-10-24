package com.github.restart1025.service;

import com.alibaba.fastjson.JSONObject;
import com.github.restart1025.entity.UploadFile;

import java.util.List;
import java.util.Map;

public interface UploadFileSerivce {
    /**
     * 根据上传人查找所上传的文件
     * @param map key:personId
     * @return
     */
    JSONObject queryUploadFilesByPersonId(Map<String, Object> map);

    /**
     * 查找所有的文件
     * @return
     */
    List<UploadFile> findAll();

    /**
     * 插入文件数据
     * @param uploadFile
     */
    void insert(UploadFile uploadFile);
}
