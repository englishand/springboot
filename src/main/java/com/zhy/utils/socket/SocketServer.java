package com.zhy.utils.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadFactory;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SocketServer {

    private static Socket socket = null;//主机与客户机的通信套接字

//    @Test
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8188);
            while (true){
                serverSocket.setSoTimeout(0);//等待客户的连接时间--SocketTimeoutException,0表示永不会超时
                socket = serverSocket.accept();
                new SocketServer().run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
    public void run() {
        log.info("当前线程："+ Thread.currentThread().getName());
        try {
            InputStream inputStream = socket.getInputStream();
            int available = 0;
            while (available==0){
                available = inputStream.available();
            }
            byte[] bContent = new byte[available];
            int i=0;
            if ((i=inputStream.read(bContent,0,bContent.length))<=0){
                log.error("读取传输信息异常。");
            }
            String reqContent = new String(bContent, StandardCharsets.UTF_8);
            log.info("获取到的请求报文为："+reqContent);

            //todo 业务处理

            OutputStream os = socket.getOutputStream();
            String result = "处理成功le";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(result.length());
            baos.write(result.getBytes());
            os.write(baos.toByteArray());
            os.flush();
        }catch (Exception e){
            log.error("socket处理异常",e);
        }
    }

    /**
     * 1.ServerSocket server = new ServerSocket(port,3);//在Server类中，连接请求队列的长度为3
     *   Client试图与Server进行100次连接，当队列中有了3个连接请求时，如果client再请求连接，就会被server拒绝。
     *
     * 2.ServerSocket的accept()方法从连接请求队列中取出一个客户的连接请求，然后创建与客户连接的Socket对象，并返回。如果队列中没有连接请求，
     *  accept()方法会一直等待，直到接受到连接请求才返回。
     *
     * 3.inputStream.available();该方法用于网络通讯，如Socket通讯时，server接收到的字节很有可能比client发送过来的数据字节少，
     *      这是因为网络通信是间断性的，一串字节往往会分批进行发送。当调用该方法得到0时，client可能已经响应了。
     *      解决方法：
     *      int count=0;
     *      while(count == 0){
     *          count = inputStream.available();
     *      }
     *      byte[] bytes = new byte[count];
     *      inputStream.read(bytes);
     */
}
