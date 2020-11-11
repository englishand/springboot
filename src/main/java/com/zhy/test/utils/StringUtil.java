package com.zhy.test.utils;

public class StringUtil {

    /**
     * 将字符串首字母大写
     * @param targetStr
     * @return
     */
    public static String upperFirstChar(String targetStr){
        if (targetStr.length()>0){
            String firstChar = targetStr.substring(0,1);
            firstChar = firstChar.toUpperCase();
            return firstChar+targetStr.substring(1);
        }
        return "";
    }
}
