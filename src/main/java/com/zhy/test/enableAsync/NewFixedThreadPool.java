package com.zhy.test.enableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewFixedThreadPool {

    private static ExecutorService threadpool;

    static {
        threadpool = Executors.newFixedThreadPool(10);
    }

    class SendMsgRunnable implements Runnable{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    }

    public void sendMsg(){
        SendMsgRunnable msgRunnable = new SendMsgRunnable();
        threadpool.execute(msgRunnable);
    }

    public static void main(String[] args){
        NewFixedThreadPool commonThread = new NewFixedThreadPool();
        commonThread.sendMsg();
        commonThread.sendMsg();
    }
}
