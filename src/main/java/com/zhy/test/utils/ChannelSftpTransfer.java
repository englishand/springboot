package com.zhy.test.utils;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Vector;

/**
 * 通过ChannelSftp对服务器进行连接、命令操作。
 */
@Slf4j
public class ChannelSftpTransfer {


    private static String host = "130.1.10.10";
    private static int port = 22;
    private static String userName = "ywguoqingkai@root@130.1.10.241";
    private static String password = "Abcd1234";
    private static String privateKey;//密钥文件路径
    private static String passPhrase;//密钥的密码

    private static ChannelSftp sftp;
    private static Channel channel;
    private static Session session;

    ChannelSftpTransfer(){
        login();
    }

    /**
     * 将文件流上传到sftp服务器
     * @param directory    上传至sftp服务器路径
     * @param sftpFileName  上传至sftp服务器后的文件名
     * @param input     文件流
     * @throws SftpException
     */
    public void upload(String directory,String sftpFileName,InputStream input) throws SftpException {
        long start = System.currentTimeMillis();
        try {
            if (sftp.ls(directory)==null){
                sftp.mkdir(directory);
            }
            sftp.cd(directory);

            sftp.put(input,sftpFileName);
            log.info("文件上传成功！！，耗时：{}ms",(System.currentTimeMillis()-start));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            logout();
        }
    }

    /**
     * 上传单个文件
     * @param directory 上传至sftp服务器路径
     * @param uploadFileUrl 目标文件路径
     */
    public void upload(String directory,String uploadFileUrl){
        File file = new File(uploadFileUrl);
        try {
            this.upload(directory,file.getName(),new FileInputStream(file));
        } catch (Exception e) {
            log.error("文件上传异常",e);
        }
    }

    /**
     * 将byte[] 上传至sftp服务器
     * @param directory
     * @param sftpFileName
     * @param bytes
     */
    public void upload(String directory,String sftpFileName,byte[] bytes){
        try {
            this.upload(directory,sftpFileName,new ByteArrayInputStream(bytes));
        } catch (SftpException e) {
            log.error("上传文件字节数组异常！！",e);
        }
    }

    /**
     * 将字符串按指定编码上传到sftp服务器上
     * @param directory     服务器路径
     * @param sftpFileName  上传服务器后的文件名
     * @param dataStr       上传的字符串
     * @param charsetName   编码
     */
    public void upload(String directory ,String sftpFileName,String dataStr,String charsetName){
        try {
            this.upload(directory,sftpFileName,new ByteArrayInputStream(dataStr.getBytes()));
        } catch (SftpException e) {
            log.error("将文件字符串上传到sftp服务器失败");
        }
    }

    /**
     * 下载文件
     * @param directory     sftp服务器上文件的路径
     * @param downloadFile  sftp服务器上文件名
     * @param saveFile      保存到本地路径
     */
    public void download(String directory,String downloadFile,String saveFile){
        try {
            if (StringUtils.isNotEmpty(directory)){
                sftp.cd(directory);
            }
            File file = new File(saveFile);
            sftp.get(downloadFile,new FileOutputStream(file));

            log.info("下载文件成功");
        } catch (Exception e) {
            log.error("下载文件失败",e);
        }finally {
            logout();
        }
    }

    /**
     * 下载文件流
     * @param directory     sftp服务器上文件目录
     * @param downloadFile  sftp服务器上文件名
     * @return  文件输入流
     */
    public InputStream downloadStream(String directory,String downloadFile){
        try {
            if (StringUtils.isNotEmpty(directory)){
                sftp.cd(directory);
            }
            return sftp.get(downloadFile);
        } catch (Exception e) {
            log.error("下载文件流异常",e);
        }finally {
            logout();
        }
        return null;
    }

    /**
     * 下载文件的字节数组
     * @param directory     sftp服务器操作路径
     * @param downloadFile  sftp服务器上的文件名
     * @return  字节数组
     */
    public byte[] download(String directory,String downloadFile){
        try {
            if (StringUtils.isNotEmpty(directory)){
                sftp.cd(directory);
            }

            InputStream inputStream = sftp.get(downloadFile);
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            logout();
        }
        return null;
    }

    /**
     * 删除文件
     * @param directory         sftp服务器上文件目录
     * @param deleteFileName    sftp服务器上的文件
     */
    public void delete(String directory,String deleteFileName){
        try {
            if (StringUtils.isNotEmpty(directory)){
                sftp.cd(directory);
            }

            sftp.rm(deleteFileName);
        } catch (Exception e) {
            log.error("删除文件失败！！！",e);
        }finally {
            logout();
        }
    }

    /**
     * 删除文件
     * @param directory     sftp服务器文件的所在文件夹目录
     */
    public void delete(String directory){
        try {
            Vector vector = this.listFiles(directory);
            for (Object v:vector){
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry)v;
                sftp.cd(directory);
                sftp.rm(lsEntry.getFilename());
            }
            log.info("删除文件夹成功");
        } catch (Exception e) {
            log.error("删除文件夹异常!!!",e);
        }finally {
            logout();
        }
    }

    /**
     * 获取文件夹下的文件
     * 单独调用该方法时，需要关闭sftp服务器连接
     * @param directory sftp服务器上的文件目录
     * @return
     */
    public Vector<?> listFiles(String directory){
        try {
            if (isDirExist(directory)){
                Vector<?> vector = sftp.ls(directory);
                //移除上级目录和根目录："."".."
                vector.remove(0);
                vector.remove(0);
                return vector;
            }
        }catch (Exception e){
            log.error("获取文件夹信息异常！！！",e);
        }finally {
//            logout();
        }
        return null;
    }

    /**
     * 执行shell命令
     * @param command
     * @return
     */
    public boolean exeCommand(String[] command){
        boolean result = false;
        InputStream input = null;
        OutputStream output = null;
        try {

            //设置sftp通信通道
            channel = (Channel)session.openChannel("shell");
            channel.connect(1000);

            //获取输入输出流
            input = channel.getInputStream();
            output = channel.getOutputStream();

            //发送需要执行的shell命令，\n:表示回车
            for (int i=0;i<command.length;i++){
                String shellCommand = command[i]+" \n";
                output.write(shellCommand.getBytes());
                output.flush();
            }
            Thread.sleep(10000);
            //获取命令执行结果
            if (input.available()>0){
                byte[] date = new byte[input.available()];
                int nlen = input.read(date);
                if (nlen<0){
                    throw new RuntimeException("network error");
                }

                //转换输出
                String temp = new String(date,0,nlen,"iso8859-1");
                log.info(temp);
            }
        } catch (Exception e) {
            log.error("执行服务器命令异常:{}",e.getMessage());
        }finally {
            try {
                output.close();
                input.close();
            } catch (IOException e) {
                log.error("输出流关闭异常:{}",e.getMessage());
            }

            channel.disconnect();
            logout();
        }
        return result;
    }

    /**
     * 判断文件或目录是否存在
     * @param path   sftp服务器上的文件或目录
     * @return
     */
    public boolean isExist(String path){
        boolean isExist = false;
        try {
            sftp.lstat(path);
            isExist = true;
            log.info("文件或目录存在");
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")){
                log.error("文件或目录不存在");
            }
        }finally {
            logout();
        }
        return isExist;
    }

    /**
     * 判断目录是否存在
     * @param directory
     * @return
     */
    public boolean isDirExist(String directory){
        try {
            SftpATTRS sftpATTRS = this.sftp.lstat(directory);
            return sftpATTRS.isDir();
        } catch (SftpException e) {
            if (e.getMessage().toLowerCase().equals("no such file")){
                log.error("文件不存在");
            }
        }
        return false;
    }

    /**
     * 判断文件夹是否存在
     * @param directory
     * @return
     */
    public boolean booleanUrl(String directory){
        try {
            if (this.sftp.ls(directory)==null){
                return false;
            }
        } catch (SftpException e) {
            log.error("检查文件夹异常",e);
            return false;
        }finally {
            logout();
        }
        return true;
    }

    /**
     * 目录不存在时创建目录
     * @param path  目录
     */
    public void mkdirs(String path){
        if (StringUtils.isNotEmpty(path)){
            File file = new File(path);
            String fs = file.getParent();
            File parentFile = new File(fs);
            if (!parentFile.exists()){
                file.mkdirs();
            }
        }
    }

    /**
     * 创建一个文件目录
     * @param createPath    路径
     * @return
     */
    public boolean createDir(String createPath){
        try {
            if (isDirExist(createPath)){
                this.sftp.cd(createPath);
                return true;
            }
            String pathArray[] = createPath.split("/");
            StringBuilder filepath = new StringBuilder("/");
            for (String path:pathArray){
                if ("".equals(path)){
                    continue;
                }
                filepath.append(path+"/");
                if (!this.isDirExist(filepath.toString())) {
                    this.mkdirs(filepath.toString());
                }
                this.sftp.cd(filepath.toString());
            }

        }catch (Exception e){
            log.error("创建文件目录失败");
            return false;
        }finally {
            logout();
        }
        return true;
    }

    /**
     * 建立连接
     * @param
     * @return
     */
    public static boolean login(){
        try {
            JSch jSch = new JSch();
            //设置密钥和密码
            if (StringUtils.isNotEmpty(privateKey) && StringUtils.isNotEmpty(passPhrase)){
                //带口令的密钥
                jSch.addIdentity(privateKey,passPhrase);
            }else if (StringUtils.isNotEmpty(privateKey)){
                //不带口令的密钥
                jSch.addIdentity(privateKey);
            }
            if (port<=0){
                //采用默认端口
                session = jSch.getSession(userName,host);
            }else {
                session = jSch.getSession(userName,host, port);
            }
            if (session == null){
                throw new RuntimeException("session is null");
            }
            //设置第一次登录时候的提示，可选值（ask | yes | no）
            //防止出现：ssh连接时发现known_hosts文件没有指纹 的问题。UnknownHostKey: 130.1.10.241. RSA key fingerprint is xxx
            session.setConfig("StrictHostKeyChecking","no");
            if (password!=null){
                //设置登录主机的密码
                session.setPassword(password);
            }
            session.setTimeout(30000);
            //设置登录超时时间
            session.connect();

            //创建sftp通信通道
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;

        }catch (Exception e){
            log.error("登录服务器异常，{}",e.getMessage());
            throw new RuntimeException("登录服务器异常",e);
        }
        log.info("sftp服务器连接成功");
        return true;
    }

    /**
     * 关闭sftp连接
     */
    public void logout(){
        try {
            if (sftp!=null){
                if (sftp.isConnected()){
                    sftp.disconnect();
                    log.info("sftp is close already");
                }
            }
            if (session!=null){
                if (session.isConnected()){
                    session.disconnect();
                    log.info("session is close already");
                }
            }
        }catch (Exception e){
            log.error("关闭ChannelSftp连接异常，{}",e.getMessage());
            throw new RuntimeException("关闭ChannelSftp连接异常",e);
        }
        log.info("已成功关闭ChannelSftp连接");
    }

    public static void main(String[] args){
        ChannelSftpTransfer transfer = new ChannelSftpTransfer();
        try {
//            String commond = "cd /appdata/zhy; ls; mv test.txt /appdata/zhy/test2.txt";
//            String[] commonds = commond.split(";");
//            transfer.exeCommand(commonds);

//            transfer.upload("/appdata/zhy","D:/赵洪友/新电商平台/银联改造/new 2.txt");

//            transfer.download("/appdata/zhy","new 2.txt","D:/赵洪友/新电商平台/银联改造/new2.txt");

//            boolean isExist = transfer.isExist("/appdata/zhy/new 2.txt");

//            Vector vector = transfer.listFiles("/appdata/zhy");

//            transfer.delete("/20201225/10/46/1/2");

            transfer.createDir("/appdata/zhy/20201225-2");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }
}
