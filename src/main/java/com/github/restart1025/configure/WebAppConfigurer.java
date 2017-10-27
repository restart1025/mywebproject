package com.github.restart1025.configure;

import com.github.restart1025.interceptor.MyInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    /**
     * 配置拦截器
     * Spring提供了基础类WebMvcConfigurerAdapter，只需要重写 addInterceptors 方法添加注册拦截器。
     *
     * 实现自定义拦截器只需要3步.
     * 1、创建我们自己的拦截器类并实现 HandlerInterceptor 接口。
     * 2、创建一个Java类继承WebMvcConfigurerAdapter，并重写 addInterceptors 方法。
     * 3、实例化我们自定义的拦截器，然后将对像手动添加到拦截器链中（在addInterceptors方法中添加）。
     *
     * 只有经过DispatcherServlet 的请求，才会走拦截器链，我们自定义的Servlet 请求是不会被拦截的
     *
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/*");

        super.addInterceptors(registry);

    }

}
