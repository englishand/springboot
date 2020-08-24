package com.zhy.test.test;

public class Test {

    public static void main(String[] args){

        String pattern = "/login/in";
        String path = "login/in";
        String seprator = "/";

        System.out.println(pattern.endsWith(seprator));
        System.out.println(path.endsWith(seprator));
        System.out.println(pattern.startsWith(seprator,7));
        System.out.println(path.startsWith(seprator,5));
    }
}
