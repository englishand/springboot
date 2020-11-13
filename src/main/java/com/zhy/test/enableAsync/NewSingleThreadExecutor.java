package com.zhy.test.enableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单例化的executor,创建及使用唯一的线程执行任务。按照指定顺序：FIFO,LIFO,优先级执行
 */
public class NewSingleThreadExecutor {

    public static void main(String[] args){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i=0;i<10;i++){

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(System.currentTimeMillis());
                        System.out.println(Thread.currentThread().getName());
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    }
}
