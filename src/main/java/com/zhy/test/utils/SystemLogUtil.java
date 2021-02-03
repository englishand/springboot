package com.zhy.test.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * 系统日志，将日志写入log.txt
 */
public class SystemLogUtil {

    private static SystemLogUtil logger = null;
    //主目录地址
    private String rootName = System.getProperty("user.dir").replace("\\","/")+"/logger/";
    //日期格式
    private String dateFormat = "yyyy-MM-dd";
    //日志等级
    private String errorLevel = "ERROR";
    private String infoLevel = "INFO";

    static {
        logger = new SystemLogUtil();
    }

    public static synchronized SystemLogUtil getLogger(){
        if (logger==null){
            logger = new SystemLogUtil();
        }
        return logger;
    }

    /**
     * 写入日志文件
     * @param log
     * @param fileName
     */
    public void info(String log,String fileName){
        String sf = String.format("[%tT] [%s] %s ",new Date(),infoLevel,log);
        try {
            write(sf,infoLevel,fileName);
        }catch (IOException e){
            System.err.println("INFO模式写入日志文件失败。"+e.getMessage()+":"+e.getCause());
        }
    }

    /**
     * 写入error级日志
     * @param log
     * @param fileName
     * @param err
     */
    public void error(String log,String fileName,Throwable err){
        if (err!=null){
            log = log+"."+err.getMessage()+":"+err.getCause();
        }else {
            log = log+ ".";
        }
        String sf = String.format("[%tT] [%s] %s",new Object[]{new Date(),errorLevel,log});
        try {
            write(sf,errorLevel,fileName);
        }catch (IOException e){
            System.err.println("写入error日志文件失败。"+e.getMessage()+":"+e.getCause());
        }
    }

    /**
     * 写入日志文件
     * @param log
     * @param level
     * @param fileName
     */
    public synchronized void write(String log,String level,String fileName) throws IOException{
        String dirName = StringUtil.getFormatDate(dateFormat);
        if (dirName.contains("-")){
            dirName = dirName.replaceAll("-","/");
        }

        //检查文件目录是否存在
        if (!isExist(dirName,fileName)){
            createFile(dirName,fileName);
        }
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(rootName+dirName+File.separator+fileName,true);
            fos.write((log+"\n").getBytes());
            fos.flush();

            if (errorLevel.equals(level)){
                System.err.println(log);
            }else System.out.println(log);
        }catch (FileNotFoundException e){
            throw e;
        }catch (IOException e){
            throw e;
        }finally {
            if (fos!=null){
                try {
                    fos.close();
                }catch (IOException e){
                    System.err.println("关闭文件流失败");
                }
            }
        }
    }

    /**
     * 判断文件目录是否存在
     * @param dirName
     * @param fileName
     * @return
     */
    private boolean isExist(String dirName,String fileName){
        File directory = new File(rootName+dirName);
        if (directory.isDirectory()){
            File file = new File(rootName+dirName+File.separator+fileName);
            if (file.isFile()){
                if (file.length()>=104857600L){
                    file.renameTo(new File(rootName+dirName+File.separator+
                            StringUtil.conversionDateFormat()+"_"+fileName));
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 创建文件目录
     * @param dirName
     * @param fileName
     */
    private void createFile(String dirName,String fileName){
        if (dirName==null || fileName==null){
            System.err.println("文件夹或文件名为空");
            return;
        }
        File root = new File(rootName);
        try {
            if (!root.isDirectory() && !root.mkdir()){
                System.err.println("无法创建日志文件的根目录：logger/.程序退出");
                System.exit(0);
            }
        }catch (SecurityException e){
            System.err.println("创建日志文件出错（权限不足）。"+e.getMessage());
            System.err.println("程序退出");
            System.exit(0);
        }

        File floader = new File(rootName+dirName);
        if (root.isDirectory() && !floader.isDirectory()){
            try {
                if (!floader.mkdirs()){
                    System.err.println("无法创建日志文件夹：logger/"+dirName);
                    System.err.println("程序退出");
                    System.exit(0);
                }
            }catch (SecurityException e){
                System.err.println("创建日志文件出错（权限不足）。"+e.getMessage());
                System.err.println("程序退出");
                System.exit(0);
            }
        }

        File file = new File(floader+File.separator+fileName);
        if (floader.isDirectory() && !file.isFile()){
            try {
                if (!file.createNewFile()){
                    System.err.println("创建日志文件失败："+fileName);
                    System.err.println("程序退出");
                    System.exit(0);
                }
            }catch (SecurityException e){
                System.err.println("创建日志文件出错（权限不足）。"+e.getMessage());
                System.err.println("程序退出");
                System.exit(0);
            }catch (IOException e){
                System.err.println("创建日志文件出错（权限不足）。"+e.getMessage());
                System.err.println("程序退出");
                System.exit(0);
            }
        }

        if (file.exists()){
            return;
        }
    }
}
