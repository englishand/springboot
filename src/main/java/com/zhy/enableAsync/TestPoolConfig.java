package com.zhy.enableAsync;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestPoolConfig {

    @Autowired
    private PoolConfig poolConfig;

    /**
     *  测试ThreadPoolTaskExecutor
     * @throws Exception
     */
    @Async("poolexecutor")//指定线程池名
    @Test
    public void one() {
        try {
            log.info("测试一");
            Long start = System.currentTimeMillis();
            poolConfig.executor().execute(new Runnable() {
                @Override
                public void run() {
                    log.info("当前线程的名称为："+Thread.currentThread().getName()+"|");
                }
            });
            for (int i=0;i<5;i++){
                Thread.sleep(2000);
                log.info("当前线程："+ Thread.currentThread().getName());
            }

            Long end = System.currentTimeMillis();
            log.info("用时: "+(end-start)+"毫秒");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 测试缓存线程池CachedThreadPool
     */
    @Test
    public void main2(){
        ExecutorService executor = poolConfig.cachedThreadPool();
        for (int i=0;i<10;i++){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int finalIndex = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    log.info(Thread.currentThread().getName()+"|"+finalIndex +"|"+System.currentTimeMillis());
                }
            });
        }
    }

    /**
     * 测试scheduleThreadPool
     */
    @Test
    public void main4(){
        ScheduledExecutorService executor = poolConfig.scheduleThreadPool();
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                log.info("");
            }
        },2000, TimeUnit.SECONDS);
    }

}
