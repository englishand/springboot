package com.zhy.test.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * 通过ftp连接方式对服务器进行连接、文件上传、下载、删除工作。
 */
@Slf4j
public class FtpTransfer {

    private FTPClient client = new FTPClient();
    private static String ftpHost = "130.1.10.10";
    private static int ftpPort = 21;
    private static String userName = "ywguoqingkai@root@130.1.10.241";
    private static String password = "Abcd1234";
    private static String workingDirectory = "/appdata/zhy";//操作目录

    /**
     * 上传文件到ftp服务器
     * @param sourcePath 文件路径
     * @param targetFileName    文件名称
     * @return  上传结果
     */
    public boolean upload(String sourcePath,String targetFileName)  {
        InputStream is = null;
        boolean result = false;
        try{
            if (login(client)){
                log.info(sourcePath+File.separator+targetFileName);
                is = new FileInputStream(new File(sourcePath+File.separator+targetFileName));
                result = client.storeFile(targetFileName,is);
            }
            client.logout();
        }catch (Exception e){
            log.error("上传文件到ftp服务器异常：{}",e.getMessage());
            throw new RuntimeException("上传文件到ftp服务器异常",e);
        }finally {
            try {
                if (is!=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            disConnect();
        }
        return result;
    }

    /**
     * 下载ftp服务器文件到本地
     * @param fileName  文件名称
     * @return  文件流
     */
    public InputStream retrieveFile(String fileName){
        InputStream input = null;
        try {

            if (login(client)){
                input = client.retrieveFileStream(fileName);
            }
            client.logout();
        }catch (Exception e){
            log.error("下载ftp服务器文件异常：{}",e.getMessage());
            throw new RuntimeException("下载ftp服务器文件异常",e);
        }finally {
            disConnect();
        }
        return input;
    }

    /**
     * 下载ftp服务器文件到本地路径下
     * @param fileName
     * @param localPath
     * @return
     */
    public boolean retrieveFileToLocal(String fileName,String localPath){
        boolean result = false;
        try {
            File file = new File(localPath);
            if (!file.exists()){
                file.mkdirs();
            }
            if (login(client)){
                //列出该目录下所有的文件
                FTPFile[] files = client.listFiles();
                for (FTPFile ff:files){
                    if (ff.getName().equals(fileName)){
                        //输出流
                        FileOutputStream fos = new FileOutputStream(localPath+File.separator+fileName);
                        //下载文件
                        result = client.retrieveFile(fileName,fos);
                        fos.close();
                    }
                }

                client.logout();
            }
        } catch (Exception e) {
            log.error("下载ftp服务器文件到本地异常:{}",e.getMessage());
            throw new RuntimeException("下载ftp服务器文件到本地异常",e);
        }finally {
            disConnect();
        }
        return result;
    }

    /**
     * 从指定目录下载文件到本地
     * @param sourcePath
     * @param fileName
     * @param localPath
     * @return
     */
    public boolean retrievePathFileToLocal(String sourcePath,String fileName,String localPath){
        boolean result = false;
        try {
            if (login(client)){
                client.changeWorkingDirectory(sourcePath);
                FTPFile[] files = client.listFiles();
                if (files!=null){
                    FileOutputStream fos = new FileOutputStream(new File(localPath+File.separator+fileName));
                    for (FTPFile file:files){
                        if (fileName.equals(file.getName())){
                            result = client.retrieveFile(fileName,fos);
                        }
                    }

                    fos.close();
                }

                client.logout();
            }
        }catch (Exception e){
            log.error("从指定目录下载文件异常",e);
            throw new RuntimeException("从指定目录下载文件异常",e);
        }finally {
            disConnect();
        }
        return result;
    }

    /**
     * 删除ftp服务器上文件
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName){
        boolean result = false;
        try {
            if (login(client)){
                result = client.deleteFile(fileName);

                client.logout();
            }
        }catch (Exception e){
            log.error("删除ftp服务器上文件异常",e);
            throw new RuntimeException("删除ftp服务器文件异常",e);
        }finally {
            disConnect();
        }
        return result;
    }

    /**
     *  监测ftp服务器上是否存在该文件
     * @param fileName
     * @return
     */
    public boolean checkFile(String fileName){

        try {
            if (login(client)){
                String[] names = client.listNames();
                for (String name:names){
                    if (fileName.equals(name)){
                        return true;
                    }
                }

                client.logout();
            }
        }catch (Exception e){
            log.error("监测ftp服务器文件出错",e);
            throw new RuntimeException("监测ftp服务器文件异常",e);
        }finally {
            disConnect();
        }
        return false;
    }


    /**
     * 登录ftp服务器
     * 协议：ftp或sftp 对应端口: 21/22
     *  登录异常：org.apache.commons.net.MalformedServerReplyException: Could not parse response code.
     *          Server Reply: SSH-2.0-OpenSSH_6.6.1
     *  Java使用org.apache.commons.net.ftp.FTPClient通过协议ssh2进行sftp连接时，就会出现如上错误，原因：1.它不支持这种方式的连接。2.host地址写错
     *  解决方法，1：继续使用org.apache.commons.net.ftp.FTPClient工具，将服务器开启SFTP协议。
     *          2：使用com.jcraft.jsch.ChannelSftp代替org.apache.commons.net.ftp.FTPClient。
     *          这里使用第二种，转至：ChannleSftpTransfer。
     * @param client ftp客户端
     * @throws Exception
     */
    public boolean login(FTPClient client) throws Exception{
        log.info("Connect to Ftp [{}:{}] with user [{}] and passwd [{}].",
                ftpHost, ftpPort, userName, password);
        //连接服务器
        try {
            client.connect(ftpHost,ftpPort);
            log.info("connected");
            //获取连接返回码
            int reply = client.getReplyCode();
            log.info("FTP server reply is "+ reply);
            //判断返回码是否合法
            if (!FTPReply.isPositiveCompletion(reply)){
                client.disconnect();
                throw new Exception("FTP server reply is not positive ,do disconnected");
            }
            //设置连接超时时间
            client.setConnectTimeout(60000);
            //登录服务器
            if (!client.login(userName,password)){
                throw new Exception("login ftp server failed");
            }

            //文件传输模式：主动模式和被动模式，这里使用被动模式
            client.enterLocalPassiveMode();
            //创建目录
            client.makeDirectory(workingDirectory);
            //设置文件操作目录
            if (!client.changeWorkingDirectory(workingDirectory)){
                throw new Exception("change workingDirectory failed");
            }
            //设置文件类型,二进制
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            //设置字符编码
            client.setControlEncoding("utf-8");
        }catch (Exception e){
            log.error("登录ftp服务器异常,{}",e.getMessage());
            throw new RuntimeException("登录ftp服务器异常",e);
        }

        log.info("连接服务器成功");
        return true;
    }

    /**
     * 退出ftp服务器
     */
    public void disConnect(){

        if (client.isConnected()){
            try {
                client.disconnect();
            } catch (IOException e) {
                log.error("关闭ftp服务器连接异常:{}",e.getMessage());
                throw new RuntimeException("关闭ftp服务器连接异常",e);
            }
        }
    }


    public static void main(String[] args){
        FtpTransfer transfer = new FtpTransfer();
        try {
            transfer.upload("D:/赵洪友/新电商平台/笔记","dpay.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
