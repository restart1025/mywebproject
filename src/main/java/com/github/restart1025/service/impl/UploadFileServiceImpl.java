package com.github.restart1025.service.impl;

import com.github.restart1025.dao.UploadFileDao;
import com.github.restart1025.entity.UploadFile;
import com.github.restart1025.service.UploadFileSerivce;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service()
public class UploadFileServiceImpl implements UploadFileSerivce {

    @Resource
    private UploadFileDao uploadFileDao;

    @Override
    public List<UploadFile> queryUploadFilesByPersonId(Map<String, String> map) {
        return uploadFileDao.queryUploadFilesByPersonId(map);
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
