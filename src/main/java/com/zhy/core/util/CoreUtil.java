package com.zhy.core.util;

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
