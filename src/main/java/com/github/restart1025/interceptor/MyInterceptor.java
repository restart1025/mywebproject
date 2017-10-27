package com.github.restart1025.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {

    /**
     * Controller方法调用之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(">>>MyInterceptor1>>>>>>>" +
                "在请求处理之前进行调用 ( Controller方法调用之前 ) ");
        return true;
    }

    /**
     * Controller方法调用之后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        System.out.println(">>>MyInterceptor1>>>>>>>请求处理之后进行调用，" +
                "但是在视图被渲染之前 ( Controller方法调用之后 ) ");

    }

    /**
     * DispatcherServlet 渲染了对应的视图之后执行 (主要是用于进行资源清理工作)
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，" +
                "也就是在DispatcherServlet 渲染了对应的视图之后执行" +
                " ( 主要是用于进行资源清理工作 ) ");

    }
}
