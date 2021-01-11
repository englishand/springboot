package com.zhy.test.intercepor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截器HandlerInterceptorAdapter
@Component
@Slf4j
public class GlobalIntercepor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器预处理开始。。。。。。。。");
        log.info(request.getRequestURL().toString());
        return super.preHandle(request, response, handler);
    }

    /**
     * 调用了Service并返回ModelAndView，但未进行页面渲染
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("拦截器后处理开始。。。。。。。。。。");
        if (modelAndView!=null){
            modelAndView.addObject("testCode","这是测试Interceptor拦截器");
            System.out.println("可以对modelAndView进行设置。。。。。。。。。。。。。。。");
        }
        log.info("拦截器后处理结束。。。。。。。。。。");
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 已经渲染了页面,可以根据ex是否为null判断是否发生了异常，进行日志记录。
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
