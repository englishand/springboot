package com.zhy.test;

import net.sf.cglib.core.DebuggingClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    private Logger logger = LoggerFactory.getLogger(LockTest.class);
    //ReentrantLock为Lock的唯一实现类
    private Lock lock = new ReentrantLock();
    private int i=0;

    //乐观锁实现方式，多个线程使用同一个atomicInteger
    private AtomicInteger atomicInteger = new AtomicInteger();

    //测试使用lock方法：如果锁已经被其他线程获取到，则等待
    public void testLock(Thread thread){
        try{
            int j=1;
            logger.info(new Date()+" 线程"+thread.getName()+"第"+j+"次获取锁");
            //获取锁
            lock.lock();
            j++;
            logger.info(new Date() +" 线程"+thread.getName()+"获取到了锁,{testLock}");
            Thread.sleep(5000);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();//必须释放锁，防止死锁
            logger.info(new Date()+" 线程" + thread.getName() + " 释放了锁！");
        }
    }

    //通过该方法获取锁时，如果线程正在等待获取锁，则这个线程能响应中断，即中断线程的等待状态,不再获取锁
    public void testLockInterruptibly(Thread thread){
        try {
            System.out.println(thread.getName()+"----"+thread.isInterrupted());
            lock.lockInterruptibly();
            logger.info(new Date()+" 线程"+thread.getName()+"进行了响应中断---");
            logger.info(new Date()+" 线程" + thread.getName() + " 获取了锁！{testLockInterruptibly}");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            logger.info(new Date()+" 线程" + thread.getName() + " 释放了锁！");
        }
    }

    //如果获取成功，则返回true，如果获取失败，则返回false
    public void testTryLock(Thread thread){
        i++;
        logger.info(new Date()+" 线程"+thread.getName()+"第"+i+"次获取锁");

        if (lock.tryLock()){
            try {
                logger.info(new Date()+" 线程" + thread.getName() + " 获取了锁！{testTryLock}");
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
                logger.info(new Date()+" 线程" + thread.getName() + " 释放了锁！");
            }
        }else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            testTryLock(thread);
        }
    }

    //在获取不到锁时，会等待一段时间期限内拿不到锁，就返回false，如果一开始就拿到锁或者在等待时间内拿到锁，则返回true
    public void testTryLock_TimeUnit(Thread thread){
        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)){
                Thread.sleep(3000);
                try {
                    logger.info(new Date()+ " 线程" + thread.getName() + " 获取了锁！{testTryLock_TimeUnit}");
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                    logger.info(new Date()+" 线程" + thread.getName() + " 释放了锁！");
                }
            }else {
                // 没有获取到锁
                logger.info("线程" + thread.getName() + " 没有获取到锁！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException{
        LockTest lockTest = new LockTest();

        for (int i=4;i<8;i++){
            Thread d =new Thread(""+i){
                public void run(){
                    lockTest.testLock(Thread.currentThread());
                }
            };
//            d.start();
        }

        Thread c = new Thread("3"){
            public void run(){
                lockTest.testLock(Thread.currentThread());
            }
        };

        Thread a = new Thread("1"){
            public void run(){
//                lockTest.testLock(Thread.currentThread());
//                lockTest.testTryLock(Thread.currentThread());
//                lockTest.testTryLock_TimeUnit(Thread.currentThread());
                lockTest.testLockInterruptibly(Thread.currentThread());
            }
        };

        Thread b = new Thread("2"){
            public void run(){
                lockTest.testLockInterruptibly(Thread.currentThread());
            }
        };


        c.start();
        a.start();

        b.start();
        b.interrupt();
//        Thread.sleep(0);

    }


}
