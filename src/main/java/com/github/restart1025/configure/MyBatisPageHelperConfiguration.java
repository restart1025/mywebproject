package com.github.restart1025.configure;

import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 * Mybatis 分页查询插件配置
 */
@Configuration
public class MyBatisPageHelperConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisPageHelperConfiguration.class);

    @Bean
    public PageHelper pageHelper(){

        logger.info("注册MyBatis分页插件PageHelper");

        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();

        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);

        return pageHelper;

    }

}
