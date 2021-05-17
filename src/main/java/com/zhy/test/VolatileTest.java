package com.zhy.test;

public class VolatileTest extends Thread{

    //在flag前面加上volatile关键字，强制线程每次读取该值的时候都去“主内存”中取值，
    volatile boolean flag = false;
    int i=0;

    public void run(){
        while (!flag){
            i++;
        }
    }

    public static void main(String[] args){
        VolatileTest vt = new VolatileTest();
        vt.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vt.flag=true;
        System.out.println("stop:"+vt.i);
    }
}
