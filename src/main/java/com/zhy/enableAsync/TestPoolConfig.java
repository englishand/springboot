package com.zhy.enableAsync;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.*;

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
            ThreadPoolTaskExecutor executor = poolConfig.executor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    log.info("当前线程的名称为："+Thread.currentThread().getName()+"|");
                }
            });
            executor.shutdown();

            Long end = System.currentTimeMillis();
            log.info("用时: "+(end-start)+"毫秒");

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
            int finalIndex = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    log.info(Thread.currentThread().getName()+"|"+finalIndex +"|"+System.currentTimeMillis());
                }
            });
        }
        executor.shutdown();
    }

    /**
     * 测试scheduleThreadPool,本例使用main方法测试，在main4()测试中预期结果不会出现
     */
    public static void main(String[] args){
        ScheduledExecutorService executor = new PoolConfig().scheduleThreadPool();
        for (int i=0;i<5;i++){
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().isDaemon());//可以查看该线程是否是守护线程
                    System.out.println(new Date()+"---"+Thread.currentThread().getName()+" 这时测试scheduleThreadPool");
                }
            },3, TimeUnit.SECONDS);//这里只是和主线程有3秒的延迟时间，多线程之间没有延迟
        }
        executor.shutdown();

        System.out.println(new Date()+" 验证延迟执行---------------");

    }

    //使用主动阻塞线程来测试，结果和预期的一眼。
    @Test
    public void main4(){
        ScheduledExecutorService executor = poolConfig.scheduleThreadPool();
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(executor.isShutdown()+" 1 "+executor.isTerminated());
                log.info("使用@Test测试schedule线程");
            }
        },2, TimeUnit.SECONDS);
        System.out.println(Thread.currentThread().getName());
        System.out.println(executor.isShutdown()+" 2 "+executor.isTerminated());
        try {//主动阻塞主线程
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Test stop");
    }

    private static final long SUCCESS = 1L;
    private static Jedis jedis = new Jedis("127.0.0.1");
    //测试redis分布式锁
    @Test
    public void main5(){
        String key = "username";
        String testStr = "zhy20210910";
//        ThreadPoolTaskExecutor executor = poolConfig.executor();
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                boolean lockResult = tryLock(key,testStr);
//                System.out.println("获取锁的状态："+lockResult+"|"+"当前线程名称："+Thread.currentThread().getName());
//                lockResult = unLock(key,testStr);
//                System.out.println("解锁的状态："+lockResult+"|"+"当前线程名称："+Thread.currentThread().getName());
//            }
//        });
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                boolean lockResult = tryLock(key,testStr);
//                System.out.println("获取锁的状态："+lockResult+"|"+"当前线程名称："+Thread.currentThread().getName());
//                lockResult = unLock(key,testStr);
//                System.out.println("解锁的状态："+lockResult+"|"+"当前线程名称："+Thread.currentThread().getName());
//            }
//        });
        boolean lockResult = false;
//        lockResult = tryLock(key,testStr);
//        System.out.println("获取锁的状态："+lockResult);
//        lockResult = 1L==jedis.del(key);
        lockResult = unLock(key,testStr);
        System.out.println("解锁的状态："+lockResult);
        System.out.println("检测线程是否异步执行");
    }
    //加锁
    public static boolean tryLock(String key,String requestId){
        //使用setnx命令，不存在则保存返回1，加锁成功；如果已存在返回0，加锁失败
        return SUCCESS==jedis.setnx(key,requestId);
    }
    //删除key的lua脚本，先比较requstId是否相等，相等则删除
    private static final String DEL_SCRIPT = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
    //解锁
    public static boolean unLock(String key,String requestId){
        //删除成功表示解锁成功
        Long result = (Long) jedis.eval(DEL_SCRIPT, Collections.singletonList(key),Collections.singletonList(requestId));
        return SUCCESS==result;
    }

}
