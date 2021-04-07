package com.zhy.test;

import java.util.Date;

public class Test {

    public static void main(String[] args){
        String sf  = String.format("[%tT] [%s] %s",new Date(),"info","你好");
        System.out.println(sf);
    }
}
