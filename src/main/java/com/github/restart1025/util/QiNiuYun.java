package com.github.restart1025.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
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

public class QiNiuYun {

    private static Logger logger = LoggerFactory.getLogger(QiNiuYun.class);

    //设置好账号的ACCESS_KEY和SECRET_KEY
    private static String ACCESS_KEY = "EJ86DHmrwk44B7QDMmSsf9DiPAWpiT-VUf8EUvFM";
    private static String SECRET_KEY = "dSUqBlmtvB0-i2sgD3pNI2j_8BLPCow3pmBetvEz";

    //要上传的空间
    public static String IMAGE = "images";
    public static String FILE = "file";

    //对应下载地址
    public static String IMAGEDOWNLOAD = "http://image.restart1025.com/";
    public static String FILEDOWNLOAD = "http://file.restart1025.com/";

    //上传到七牛后保存的文件名
    //String key = UUID.randomUUID().toString();
    //上传文件的路径
    //String FilePath = "D:\\restart1025\\log\\spring.log";

    //密钥配置
    private static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    ///////////////////////指定上传的Zone的信息//////////////////
    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    private static Zone z = Zone.autoZone();
    private static Configuration c = new Configuration(z);

    //创建上传对象
    private static UploadManager uploadManager = new UploadManager(c);


    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    private static String getUpToken(String bucketname) {
        return QiNiuYun.auth.uploadToken(bucketname);
    }

    /**
     * 文件上传
     * @param file
     * @param bucketname
     * @return
     * @throws IOException
     */
    public static String upload(File file, String bucketname) throws IOException {
        if( file != null && file.exists() )
        {
            try {
                String newFileName = newFileName(file.getName());
                //调用put方法上传
                Response res = uploadManager.put(file, newFileName, getUpToken(bucketname));
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

    // 文件下载
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

    //通过文件名获取扩展名
    public static String getFileExt(String filename){
        return FilenameUtils.getExtension(filename);
    }

    //通过文件名获取没有扩展名的文件名
    public static String getFileName(String filename){
        return filename.replace("."+getFileExt(filename), "");
    }

    //生成UUID随机数，作为新的文件名
    private static String newFileName(String filename){
        String ext=getFileExt(filename);
        return UUID.randomUUID().toString().replaceAll("-", "")+"."+ext;
    }

}
