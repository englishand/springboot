package com.zhy.enableAsync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Task {

    @Async("poolexecutor")//指定线程池名
    public void one1() {
        try {
            log.info(Thread.currentThread().getName()+"|测试一");
            Long start = System.currentTimeMillis();
//            ThreadPoolTaskExecutor executor = poolConfig.executor();
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    log.info("当前线程的名称为："+Thread.currentThread().getName()+"|");
//                }
//            });
//            executor.shutdown();
            Long end = System.currentTimeMillis();
            log.info(Thread.currentThread().getName()+"用时: "+(end-start)+"毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Async("poolexecutor")//指定线程池名
    public void one2() {
        try {
            log.info(Thread.currentThread().getName()+"|测试二");
            Long start = System.currentTimeMillis();
            Long end = System.currentTimeMillis();
            log.info(Thread.currentThread().getName()+"用时: "+(end-start)+"毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Async("poolexecutor")//指定线程池名
    public void one3() {
        try {
            log.info(Thread.currentThread().getName()+"|测试三");
            Long start = System.currentTimeMillis();
            Long end = System.currentTimeMillis();
            log.info(Thread.currentThread().getName()+"用时: "+(end-start)+"毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
