package com.github.restart1025.service;

import com.alibaba.fastjson.JSONObject;
import com.github.restart1025.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

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
     * @param map
     */
    void insert(Map<String, Object> map);

    /**
     * 删除文件 ( 根据文件名和上传人 )
     * @param map fileSn, personId
     */
    void deleteFile(Map<String, Object> map);

    /**
     * 多文件上传
     * @param files 上传文件列表
     * @param personId 人员编号
     * @param savePath 保存文件到临时目录
     * @return
     */
    Map<String, Object> batchUpload(List<MultipartFile> files, String personId, String savePath);
}
