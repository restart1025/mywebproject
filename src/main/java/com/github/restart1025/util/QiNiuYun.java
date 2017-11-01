package com.github.restart1025.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 *
 * 七牛云存储所对应的文件上传下载配置
 *
 * ACCESS_KEY、SECRET_KEY : 对应凭证
 *
 * auth : 凭证所生成的秘钥
 *
 * Bucket : 上传到对应的空间名称
 *
 * DOWNLOAD : 对应空间所对应的文件下载地址
 *
 * key : 服务器上的文件名
 *
 */
public class QiNiuYun {

    private static Logger logger = LoggerFactory.getLogger(QiNiuYun.class);

    /**************机密配置, 需用文件来传输************/
    /**
     * ACCESS_KEY
     */
    private final static String ACCESS_KEY = "EJ86DHmrwk44B7QDMmSsf9DiPAWpiT-VUf8EUvFM";
    /**
     * SECRET_KEY
     */
    private final static String SECRET_KEY = "dSUqBlmtvB0-i2sgD3pNI2j_8BLPCow3pmBetvEz";
    /**
     * 密钥配置
     */
    private final static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);


    /*************上传空间和下载路径配置************/
    /**
     * 图片空间Bucket
     */
    public final static String IMAGE = "image";
    /**
     * 文件空间Bucket
     */
    public final static String FILE = "file";

    public final static String VIDEO = "video";


    /**
     * 图片空间所对应的下载地址
     */
    //public final static String IMAGEDOWNLOAD = getDownLoadAddressByBucket(QiNiuYun.IMAGE);
    /**
     * 文件空间所对应的下载地址
     */
    //public final static String FILEDOWNLOAD = getDownLoadAddressByBucket(QiNiuYun.FILE);


    /*****************指定上传的地址Zone的信息****************/
    /**
     * 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南
     */
    private final static Zone z = Zone.autoZone();
    private final static Configuration c = new Configuration(z);

    /**
     * 创建上传对象
     */
    private final static UploadManager uploadManager = new UploadManager(c);

    /**
     * 简单上传, 使用默认策略, 只需要设置上传的空间名就可以了
     * @param bucketName 服务器所对应的空间名
     * @return
     */
    private static String getUpToken(String bucketName) {
        return QiNiuYun.auth.uploadToken(bucketName);
    }

    /**
     * 文件上传
     * @param file 上传的文件
     * @param bucketName 上传文件所对应的服务器空间
     * @return 上传后服务器上的文件名
     * @throws IOException
     */
    public static String upload(File file, String bucketName) throws IOException {
        if( file != null && file.exists() )
        {
            try {
                //创建新文件名
                String newFileName = newFileName(file.getName());
                //调用put方法上传
                Response res = uploadManager.put(file, newFileName, getUpToken(bucketName));
                //打印返回的信息
                logger.info("uploadFile " + file.getName() + ", return msg : " + res.bodyString());
                return newFileName;
            } catch (QiniuException e) {
                Response r = e.response;
                // 请求失败时打印的异常的信息
                logger.error("uploadFile error msg :" + r.toString());
                //响应的文本信息
                logger.info("uploadFile error fileName :" + file.getName() + ", return msg : " + r.bodyString());
            }
        }
        return null;
    }

    /**
     * 文件下载
     * @param URL 文件的下载全地址
     * @return 文件流
     * @throws IOException
     */
    public static InputStream download(String URL) throws IOException {
        InputStream inputStream = null;
        String downloadURL = null;
        try{
            //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
            downloadURL = auth.privateDownloadUrl(URL, 3600);
            URL url = new URL(downloadURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //得到输入流
            inputStream = conn.getInputStream();
            logger.info( "downLoad file URL : " + downloadURL);
        } catch (Exception e) {
            logger.error("downLoad file URL : " + downloadURL);
            logger.error("downLoad file error msg : " + e.toString());
        }
        return inputStream;
    }

    /**
     * 文件下载
     * @param URL 文件的下载全地址
     * @return 文件File
     */
    public static File downloadReturnFile(String URL) {
        String downloadURL = null;
        try{
            //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
            downloadURL = auth.privateDownloadUrl(URL, 3600);

            logger.info( "downLoad file URL : " + downloadURL);

            return FileUtils.toFile(new URL(downloadURL));

        } catch (Exception e) {
            logger.error("downLoad file URL : " + downloadURL);
            logger.error("downLoad file error msg : " + e.toString());
        }
        return null;
    }

    /**
     * 通过文件名获取扩展名
     * @param fileName 文件名
     * @return
     */
    public static String getFileExtNoPoint(String fileName){
        return FilenameUtils.getExtension(fileName);
    }

    /**
     * 通过文件名获取没有扩展名的文件名
     * @param fileName 文件名
     * @return
     */
    public static String getFileNameNoPointExt(String fileName){
        return fileName.replace("." + getFileExtNoPoint(fileName), "");
    }

    /**
     * 生成UUID随机数, 作为新的文件名
     * @param fileName 文件名
     * @return
     */
    private static String newFileName(String fileName){
        String ext = getFileExtNoPoint(fileName);
        return UUID.randomUUID().toString().replaceAll("-", "")+"."+ext;
    }

    /**
     * 根据空间Bucket来获取下载地址
     * @param bucket
     * @return
     */
    public static String getDownLoadAddressByBucket(String bucket) {
        StringBuffer stringBuffer = new StringBuffer("http://");
        stringBuffer.append(bucket);
        stringBuffer.append(".restart1025.com/");
        return stringBuffer.toString();
    }

    /**
     * 根据文件名称获取要上传的空间Bucket
     * @param fileName
     * @return
     */
    public static String getBucketByFileName(String fileName) {
        //判断文件类型
        String ext = getFileExtNoPoint(fileName);
        //BMP、JPG、JPEG、PNG、GIF
        String bucket="";
        switch(ext.toLowerCase()){
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
            case "pcx":
            case "tiff":
            case "tga":
            case "exif":
            case "fpx":
            case "svg":
            case "psd":
            case "cdr":
            case "pcd":
            case "dxf":
            case "ufo":
            case "eps":
            case "ai":
            case "hdri":
            case "raw":
                bucket = QiNiuYun.IMAGE;
                break;
            case "mp4":
            case "avi":
            case "rmvb":
            case "rm":
            case "asf":
            case "divx":
            case "mpg":
            case "mpeg":
            case "mpe":
            case "wmv":
            case "vob":
                bucket = QiNiuYun.VIDEO;
                break;
            default: bucket = QiNiuYun.FILE;
        }
        return bucket;
    }

}
