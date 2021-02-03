package com.zhy.test.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    /**
     * 获取本地地址
     * @return
     */
    public  String getLocalHost(){
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取时间字符串
     * @return
     */
    static String conversionDateFormat(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        return df.format(new Date());
    }

    /**
     * 获取参数格式的时间串
     * @param format
     * @return
     */
    public static String getFormatDate(String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = new Date();
            return sdf.format(date);
        }catch (IllegalArgumentException e){
            System.err.println("不符合Java规范的参数！默认参数格式：yyyy-MM-dd");
        }
        return null;
    }
}
