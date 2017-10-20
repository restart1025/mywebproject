package com.github.restart1025.entity;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class UploadFile extends BaseEntity {

    @Column(name="file_sn")
    private String fileSn;//文件编号(云盘上的文件名)
    @Column(name="file_name")
    private String fileName;//文件的上传名字
    @Column(name="file_type")
    private String fileType;//文件的类型
    @Column(name="file_path")
    private String filePath;//文件的下载路径
    @Column(name="person_id")
    private Person uploader;//文件的上传人
    @Column(name="upload_time")
    private LocalDateTime uploadTime;//上传时间

    public String getFileSn() {
        return fileSn;
    }

    public void setFileSn(String fileSn) {
        this.fileSn = fileSn;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Person getUploader() {
        return uploader;
    }

    public void setUploader(Person uploader) {
        this.uploader = uploader;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }
}
