package com.zhy.test.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 通过ChannelSftp对服务器进行连接、命令操作。
 */
@Slf4j
public class ChannelSftpTransfer {

    private JSch jSch = new JSch();
    private static String host = "130.1.10.10";
    private static int port = 22;
    private static String userName = "ywguoqingkai@root@130.1.10.241";
    private static String password = "Abcd1234";
    private static String privateKey;//密钥文件路径
    private static String passPhrase;//密钥的密码

    private static Channel channel;
    private static Session session;

    /**
     * 建立连接
     * @param jSch
     * @return
     */
    public boolean login(JSch jSch) throws Exception{
        try {
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
        }catch (Exception e){
            log.error("登录服务器异常，{}",e.getMessage());
            throw new RuntimeException("登录服务器异常",e);
        }
        log.info("连接服务器成功");
        return true;
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
            if (login(jSch)){

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
     * 登出
     */
    public void logout(){
        try {
            session.disconnect();
        }catch (Exception e){
            log.error("关闭ChannelSftp连接异常，{}",e.getMessage());
            throw new RuntimeException("关闭ChannelSftp连接异常",e);
        }
        log.info("已成功关闭ChannelSftp连接");
    }

    public static void main(String[] args){
        ChannelSftpTransfer transfer = new ChannelSftpTransfer();
        try {
            String commond = "cd /appdata/zhy; ls; mv test.txt /appdata/zhy/test2.txt";
            String[] commonds = commond.split(";");
            transfer.exeCommand(commonds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
