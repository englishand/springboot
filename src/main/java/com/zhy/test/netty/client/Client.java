package com.zhy.test.netty.client;

public class Client {

    public static void main(String[] args){
        try {
            ClientUtils.startClient("127.0.0.1",9999,new ClientHandler());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage()+":"+e.getCause());
        }
    }
}
