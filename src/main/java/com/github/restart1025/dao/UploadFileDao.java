package com.github.restart1025.dao;

import com.github.restart1025.configure.MyMapper;
import com.github.restart1025.entity.UploadFile;

import java.util.List;
import java.util.Map;

public interface UploadFileDao extends MyMapper<UploadFile> {
    /**
     * 根据上传人查找所上传的文件
     * @param map key:personId
     * @return
     */
    List<UploadFile> queryUploadFilesByPersonId(Map<String, String> map);

    /**
     * 查找所有的文件
     * @return
     */
    List<UploadFile> findAll();

    void insertUploadFile(UploadFile uploadFile);
}
