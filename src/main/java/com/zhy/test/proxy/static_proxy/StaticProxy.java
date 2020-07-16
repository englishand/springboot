package com.zhy.test.proxy.static_proxy;

public class StaticProxy {

    public static void main(String[] args){
        Singer singer = new Agent(new Star());

        singer.sing();
    }
}
