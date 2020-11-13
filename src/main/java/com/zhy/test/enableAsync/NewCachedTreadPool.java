package com.zhy.test.enableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收线程，则新建。
 * 优点：1.工作线程的数量无限制，可灵活的往线程池添加线程
 * 2.如果长时间没有往线程池提交任务，则该工作线程将自动终止。
 * 注意：控制任务的数量，由于大量线程运行，可能导致系统瘫痪
 */
public class NewCachedTreadPool {

    public static void main(String[] args){
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i=0;i<10;i++){
            try {
                Thread.sleep(i*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int finalIndex = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(finalIndex +"nihao"+System.currentTimeMillis());
                }
            });
        }
    }
}
