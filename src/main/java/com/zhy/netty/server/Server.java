package com.zhy.netty.server;

public class Server {

    public static void main(String[] args) throws InterruptedException{
        ServerUtils.startServer(9999,new ServerChannelHandler());
    }
}
