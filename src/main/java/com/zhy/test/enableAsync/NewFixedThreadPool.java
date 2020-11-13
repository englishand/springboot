package com.zhy.test.enableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 指定线程数量的线程池，如果线程数量达到最大，则将提交的任务存入 池队列中
 * 线程池在空闲时，不是释放空闲线程。
 * 优点：线程池提高程序效率和节省创建线程所耗的开销
 */
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
