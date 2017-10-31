package com.github.restart1025.entity;

import java.time.LocalDateTime;

public class UploadFile extends BaseEntity {

    private String fileSn;//文件编号(云盘上的文件名)
    private String fileName;//文件的上传名字
    private String fileType;//文件的类型
    private String filePath;//文件的下载路径
    private Person uploader;//文件的上传人
    private LocalDateTime uploadTime;//上传时间
    private String fileSize;//文件大小

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

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
