package com.zhy.netty.server;

public class Server {

    public static void main(String[] args) throws InterruptedException{
        System.out.println(System.getProperty("user.dir"));
        ServerUtils.startServer(9999,new ServerChannelHandler());
    }
}
