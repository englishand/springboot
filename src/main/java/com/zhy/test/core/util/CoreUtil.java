package com.zhy.test.core.util;

import org.springframework.stereotype.Component;

@Component
public class CoreUtil {

    public static Object getObject(Class tClass){
        try {
            return ApplicationContextUtil.getBean(tClass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
