package com.zhy.test.enableAsync;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定长线程池、支持定时以及周期性的任务执行
 */
public class NewScheduleThreadPool implements Runnable{

    static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);


    public static void main(String[] args){
        executor.schedule(new NewScheduleThreadPool(),2, TimeUnit.SECONDS);
//        executor.scheduleAtFixedRate(new NewScheduleThreadPool(),2,3,TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        System.out.println(System.currentTimeMillis());
    }
}
