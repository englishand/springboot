package com.zhy.test.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

public class StringUtil {

    static SystemLogUtil log = new SystemLogUtil();

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

    /**
     * 将文件转字符串数组
     * @param file
     * @return
     */
    public static String[] fileToString(File file){
        String msg = "";
        String[] arr = null;
        try {
            msg = FileUtils.readFileToString(file);
//            System.out.println(msg);
            arr = msg.split("\\|");

            if (null==arr || arr.length<1){
                log.error("读取文件失败","error.log",null);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static void main(String[] args){
        // 文件队列
        LinkedBlockingQueue<String> fileQueue = new LinkedBlockingQueue<String>();
        String fileName = fileQueue.poll();
        System.out.println(fileName);
    }

}
