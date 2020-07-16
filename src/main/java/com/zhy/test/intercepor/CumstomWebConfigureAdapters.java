package com.zhy.test.intercepor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
public class CumstomWebConfigureAdapters implements WebMvcConfigurer {

//    @Autowired
//    private GlobalIntercepor globalIntercepor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalIntercepor()).addPathPatterns("/**");

//        registry.addInterceptor(globalIntercepor);
    }
}
