package com.zhy.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("Application正在初始化："+applicationContext);
        this.context = applicationContext;
    }

    public static <T> T getBean(Class<T> tClass) throws Exception {
        if (context!=null){
            return context.getBean(tClass);
        }
        throw new Exception("获取对象失败");
    }

    public static ApplicationContext getApplicationContext (){
        return context;
    }
}
