package com.zhy.test.enableAsync;

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
    public void one() throws Exception{
        log.info("测试一");
        Long start = System.currentTimeMillis();
        Thread.sleep(100);
        Long end = System.currentTimeMillis();
        log.info("用时: "+(end-start)+"毫秒");
    }

    /**
     * 测试ThreadPoolTaskExecutor
     * @throws Exception
     */
    @Test
    public void main() throws Exception{
        try {
            TestPoolConfig poolThread = new TestPoolConfig();
            poolThread.one();
            Thread.sleep(3000);//为了让其他线程进行。
//            Thread.currentThread().join();//  阻塞当前调用线程，其他线程调用完后才调用
            System.out.println("主线程方法");
        } catch (Exception e) {
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

    /**
     * 测试scheduleThreadPool
     */
    @Test
    public void main4(){
        ScheduledExecutorService executor = poolConfig.scheduleThreadPool();
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("");
            }
        },2000, TimeUnit.SECONDS);
    }

}
