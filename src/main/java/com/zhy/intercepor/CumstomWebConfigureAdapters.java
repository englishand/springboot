package com.zhy.intercepor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CumstomWebConfigureAdapters implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(new GlobalIntercepor());
        //排除配置
        addInterceptor.excludePathPatterns("/login/in");
        addInterceptor.excludePathPatterns("/**/*.js");
        addInterceptor.excludePathPatterns("/**/*.css");
        addInterceptor.excludePathPatterns("/login/error");

        //拦截配置
        addInterceptor.addPathPatterns("/**");
    }
}
