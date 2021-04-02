package com.zhy.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class FileUtil {

    static SystemLogUtil log = new SystemLogUtil();

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
