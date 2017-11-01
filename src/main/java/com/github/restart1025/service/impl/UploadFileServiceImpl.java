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
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
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
            jo.put("id", uploadFile.getId());
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
            jo.put("fileSize", uploadFile.getFileSize());
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
    public JSONObject deleteFile(Map<String, Object> map) {
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
                        uploadFileDao.deleteFile(map);
                        jo.put("result", true);
//                        jo.put("msg", "成功!");
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

                    //根据文件名获取要上传的空间Bucket
                    String bucket = QiNiuYun.getBucketByFileName(saveFile.getName());
                    //上传后返回文件编号
                    String yunFileName = QiNiuYun.upload(saveFile, bucket);
                    map = new HashMap<String, Object>();
                    map.put("fileName", QiNiuYun.getFileNameNoPointExt(saveFile.getName()));
                    map.put("filePath", QiNiuYun.getDownLoadAddressByBucket(bucket) + yunFileName);
                    map.put("fileSn", yunFileName);
                    map.put("fileType", bucket);
                    map.put("uploadTime", LocalDateTime.now());
                    map.put("uploader", personId);
                    map.put("fileSize", fileSizeByteToM(saveFile.length()));
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

    @Override
    public void fileDownload(HttpServletResponse res, String filePath,
                             String fileName) throws UnsupportedEncodingException {
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
            bis = new BufferedInputStream(new FileInputStream(QiNiuYun.downloadReturnFile(filePath)));
//            bis = QiNiuYun.download(filePath);
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
        return "." + QiNiuYun.getFileExtNoPoint(fileName);
    }

    /**
     * 将文件大小由Byte转为MB或者KB
     * @return
     */
    private String fileSizeByteToM(Long size) {

        BigDecimal fileSize = new BigDecimal(size);
        BigDecimal param = new BigDecimal(1024);
        int count = 0;
        while(fileSize.compareTo(param) > 0 && count < 5)
        {
            fileSize = fileSize.divide(param);
            count++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(fileSize) + "";
        switch (count) {
            case 0:
                result += "B";
                break;
            case 1:
                result += "KB";
                break;
            case 2:
                result += "MB";
                break;
            case 3:
                result += "GB";
                break;
            case 4:
                result += "TB";
                break;
            case 5:
                result += "PB";
                break;
        }
        return result;
    }
}
