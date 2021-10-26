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
import java.util.*;
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
    @Autowired
    private Task task;
    @Test
    public void one(){
        task.one1();
        task.one2();
        task.one3();
    }



    /**
     * 测试缓存线程池CachedThreadPool
     */
    @Test
    public void one2(){
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
     * 测试scheduleThreadPool,本例使用main方法测试，在one4()测试中预期结果不会出现
     */
    public static void main(String[] args){
        ScheduledExecutorService executor = new PoolConfig().scheduleThreadPool();
        for (int i=0;i<5;i++){
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    log.info(Thread.currentThread().isDaemon()+"");//可以查看该线程是否是守护线程
                    log.info(new Date()+"---"+Thread.currentThread().getName()+" 这时测试scheduleThreadPool");
                }
            },3, TimeUnit.SECONDS);//这里只是和主线程有3秒的延迟时间，多线程之间没有延迟
        }
        executor.shutdown();

        log.info(new Date()+" 验证延迟执行---------------");

    }

    //使用主动阻塞线程来测试，结果和预期的一眼。
    @Test
    public void one4(){
        ScheduledExecutorService executor = poolConfig.scheduleThreadPool();
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                log.info(executor.isShutdown()+" 1 "+executor.isTerminated());
                log.info("使用@Test测试schedule线程");
            }
        },2, TimeUnit.SECONDS);
        log.info(Thread.currentThread().getName());
        log.info(executor.isShutdown()+" 2 "+executor.isTerminated());
//        try {//主动阻塞主线程
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        log.info("Test stop");
    }


    //测试redis分布式锁
    private static final long SUCCESS = 1L;
    private static Jedis jedis = new Jedis("192.168.110.182",6379);
    @Test
    public void one5(){
        String key = "username";
        String testStr = "zhy20210910";
        List list = new ArrayList();
        jedis.lpush("1a","a:222","112","c:44333");
        String value = jedis.lpop("1a");
        log.info(value);
//        ThreadPoolTaskExecutor executor = poolConfig.executor();
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                boolean lockResult = tryLock(key,testStr);
//                log.info("获取锁的状态："+lockResult+"|"+"当前线程名称："+Thread.currentThread().getName());
//                lockResult = unLock(key,testStr);
//                log.info("解锁的状态："+lockResult+"|"+"当前线程名称："+Thread.currentThread().getName());
//            }
//        });
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                boolean lockResult = tryLock(key,testStr);
//                log.info("获取锁的状态："+lockResult+"|"+"当前线程名称："+Thread.currentThread().getName());
//                lockResult = unLock(key,testStr);
//                log.info("解锁的状态："+lockResult+"|"+"当前线程名称："+Thread.currentThread().getName());
//            }
//        });
        boolean lockResult = false;
        lockResult = tryLock(key,testStr);
        log.info("获取锁的状态："+lockResult);
        lockResult = 1L==jedis.del(key);
        log.info("删除缓存数据结果："+lockResult);
        lockResult = unLock(key,testStr);
        log.info("解锁的状态："+lockResult);
        log.info("检测线程是否异步执行");

        jedis.close();
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
    //测试redis分布式锁结束

}
