package com.zhy.designPattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取接口实现bean,并存储到ConcurrentHashMap,通过枚举获取对应的实现bean
 * 设计模式：利用applicationContext.getBeansOfType动态加载类
 */
@Component
public class ApiFactory {

    @Autowired
    private ApplicationContext applicationContext;
    Map<String,ApiService> beans = new ConcurrentHashMap<>();

    @Bean("operations")
    public Map<String,ApiService> operations(){
        Map<String,ApiService> map = applicationContext.getBeansOfType(ApiService.class);
        for (ApiService api:map.values()){
            beans.put(api.op().getValue(),api);
        }
        return beans;
    }

    public ApiService getApi(ApiEnum apiEnum){
        return beans.get(apiEnum.getValue());
    }
}
