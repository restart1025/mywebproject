package com.github.restart1025.configure;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
public class UploadFileConfiguration {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置文件大小限制 ,超出设置页面会抛出异常信息，
        // 这样在文件上传的地方就需要进行异常信息的处理了;
        factory.setMaxFileSize("2048MB"); // KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("4096MB");
        // Sets the directory location where files will be stored.
        // factory.setLocation("路径地址");
        return factory.createMultipartConfig();
    }

}
